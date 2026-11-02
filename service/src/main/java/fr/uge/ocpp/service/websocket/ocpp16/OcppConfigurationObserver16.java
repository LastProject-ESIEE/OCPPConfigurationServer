package fr.uge.ocpp.service.websocket.ocpp16;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.service.websocket.ChargePointManager;
import fr.uge.ocpp.service.websocket.OcppMessageSender;
import fr.uge.ocpp.service.websocket.OcppObserver;
import fr.uge.ocpp.websocket.ocpp.ConfigurationTranscriptor;
import fr.uge.ocpp.websocket.ocpp.OcppMessage;
import fr.uge.ocpp.websocket.ocpp.ocpp16.BootNotificationRequest16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.BootNotificationResponse16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.ChangeConfigurationRequest16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.ChangeConfigurationResponse16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.FirmwareStatusNotificationRequest16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.FirmwareStatusNotificationResponse16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.ResetRequest16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.ResetResponse16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.UpdateFirmwareRequest16;
import fr.uge.ocpp.websocket.ocpp.ocpp16.data.FirmwareStatus;
import fr.uge.ocpp.websocket.ocpp.ocpp16.data.RegistrationStatus;
import fr.uge.ocpp.websocket.ocpp.ocpp16.data.ResetType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the OCPP 1.6 observer.<br>
 * An observer is defined by the interface {@link OcppObserver}.
 */
public class OcppConfigurationObserver16 implements OcppObserver {
  private static final Logger LOGGER = LoggerFactory.getLogger(OcppConfigurationObserver16.class);

  private final OcppMessageSender sender;
  private final ChargePointManager chargePointManager;
  private final ChargePointRepository chargePointRepository;
  private final FirmwareRepository firmwareRepository;
  private final Queue<ChangeConfigurationRequest16> queue = new LinkedList<>();
  private String firmwareVersion;
  private String targetFirmwareVersion;
  private boolean loaded = false;
  private boolean lastOrderModeOn = false;

  /**
   * {@link OcppConfigurationObserver16}'s constructor.
   *
   * @param sender the web socket sender.
   * @param chargePointManager the charge point's manager.
   * @param chargePointRepository the charge point repository.
   * @param firmwareRepository the firmware repository.
   */
  public OcppConfigurationObserver16(
      OcppMessageSender sender,
      ChargePointManager chargePointManager,
      ChargePointRepository chargePointRepository,
      FirmwareRepository firmwareRepository) {
    this.sender = Objects.requireNonNull(sender);
    this.chargePointManager = Objects.requireNonNull(chargePointManager);
    this.chargePointRepository = Objects.requireNonNull(chargePointRepository);
    this.firmwareRepository = Objects.requireNonNull(firmwareRepository);
  }

  @Override
  public Optional<OcppMessage> onMessage(OcppMessage ocppMessage) {
    if (ocppMessage == null) {
      return processDefaultMessage();
    }
    return switch (ocppMessage) {
      case BootNotificationRequest16 b -> processBootNotification(b);
      case ChangeConfigurationResponse16 c -> processConfigurationResponse(c);
      case ResetResponse16 ignored -> processResetResponse();
      case FirmwareStatusNotificationRequest16 f -> processFirmwareStatusResponse(f);
      default -> processDefaultMessage();
    };
  }

  /**
   * Processes the received {@link BootNotificationRequest16} sent by the charge point.<br>
   * It is here where we start the automation of the configuration/firmware update.
   *
   * @param bootNotificationRequest16 the {@link BootNotificationRequest16}.
   * @return an optional of the response sent.
   */
  private Optional<OcppMessage> processBootNotification(
      BootNotificationRequest16 bootNotificationRequest16) {
    firmwareVersion = bootNotificationRequest16.firmwareVersion();
    // Get charge point from database
    var found =
        chargePointRepository.findBySerialNumberChargepointAndConstructor(
            bootNotificationRequest16.chargePointSerialNumber(),
            bootNotificationRequest16.chargePointVendor());
    // If charge point is not found then skip it
    if (found.isEmpty()) {
      LOGGER.warn(
          "unknown charge point, serial number: {}",
          bootNotificationRequest16.chargePointSerialNumber());
      var response =
          new BootNotificationResponse16(
              LocalDateTime.now().toString(), 10, RegistrationStatus.Rejected);
      sender.sendMessage(response, chargePointManager);
      return Optional.of(response);
    }
    var currentChargepoint = found.orElseThrow();
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    if (currentChargepoint.configuration() == null) {
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.PENDING));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      chargePointManager.notifyStatusUpdate();
      LOGGER.info("charge point {} is authenticated", currentChargepoint.serialNumber());
      var response =
          new BootNotificationResponse16(
              LocalDateTime.now().toString(), 5, RegistrationStatus.Accepted);
      sender.sendMessage(response, chargePointManager);
      return Optional.of(response);
    }
    targetFirmwareVersion = currentChargepoint.configuration().firmware().version();
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint
                .withState(true)
                .withStatus(currentChargepoint.step(), StatusProcess.PENDING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    LOGGER.info("charge point {} is authenticated", currentChargepoint.serialNumber());
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate();
    chargePointManager.notifyOnConnection();
    // Send BootNotification Response
    var response =
        new BootNotificationResponse16(
            LocalDateTime.now().toString(), 5, RegistrationStatus.Accepted);
    sender.sendMessage(response, chargePointManager);
    return switch (currentChargepoint.step()) {
      case Step.CONFIGURATION -> processConfigurationRequest();
      case Step.FIRMWARE -> processFirmwareRequest();
    };
  }

  /**
   * Processes the configuration.<br>
   * It searches in the database the configuration for the charge point, loads the key-values and
   * sends a {@link ChangeConfigurationRequest16} for each of them to the charge point.
   *
   * @return an optional of the message sent.
   */
  private Optional<OcppMessage> processConfigurationRequest() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    if (queue.isEmpty() && !loaded) {
      loadKeyValue();
      loaded = true;
    }
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.PROCESSING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate();
    var config = queue.poll();
    if (config == null && loaded && !lastOrderModeOn) {
      prepareLastOrder();
      lastOrderModeOn = true;
      return processConfigurationRequest();
    } else if (config == null) {
      LOGGER.info(
          "configuration for the charge point {} is done !", currentChargepoint.serialNumber());
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.FINISHED));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      // Dispatch information to users
      chargePointManager.notifyStatusUpdate();
      chargePointManager.notifyProcess();
      return processResetRequest();
    } else {
      sender.sendMessage(config, chargePointManager);
      return Optional.of(config);
    }
  }

  /**
   * Processes the {@link ChangeConfigurationResponse16} sent by the charge point.<br>
   * If the change has been rejected, we stop the update, otherwise we continue.
   *
   * @param response the {@link ChangeConfigurationResponse16}.
   * @return an optional of the next message sent.
   */
  private Optional<OcppMessage> processConfigurationResponse(
      ChangeConfigurationResponse16 response) {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    return switch (response.status()) {
      case Accepted, RebootRequired -> processConfigurationRequest();
      default -> {
        LOGGER.warn(
            "configuration for the charge point {} has failed, see its status !",
            currentChargepoint.serialNumber());
        currentChargepoint =
            chargePointRepository.save(
                currentChargepoint
                    .withError(response.status().name())
                    .withStatus(currentChargepoint.step(), StatusProcess.FAILED));
        chargePointManager.setCurrentChargepoint(currentChargepoint);
        // Dispatch information to users
        chargePointManager.notifyStatusUpdate();
        chargePointManager.notifyProcess();
        queue.clear();
        yield Optional.empty();
      }
    };
  }

  /**
   * Prepares the last orders sent once every configuration key has been applied: the charge point's
   * identity and, if configured, the address of the next server ({@code FINAL_WS_SERVER_ADDRESS}).
   */
  private void prepareLastOrder() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    LOGGER.info(
        "configuration for the charge point {} is almost done, sending last order !",
        currentChargepoint.serialNumber());
    var clientId = currentChargepoint.clientId();
    var changeConfig =
        new ChangeConfigurationRequest16(
            ConfigurationTranscriptor.CHARGEPOINT_IDENTITY
                .getOcpp16Key()
                .getKeyAccordingToVersion(firmwareVersion),
            clientId);
    queue.add(changeConfig);
    var finalServerAddress = System.getenv("FINAL_WS_SERVER_ADDRESS");
    if (finalServerAddress != null) {
      changeConfig =
          new ChangeConfigurationRequest16(
              ConfigurationTranscriptor.NETWORK_PROFILE
                  .getOcpp16Key()
                  .getKeyAccordingToVersion(firmwareVersion),
              ConfigurationTranscriptor.NETWORK_PROFILE
                  .getOcpp16Key()
                  .getValueFormatAccordingToVersion(firmwareVersion)
                  .formatted(finalServerAddress));
      queue.add(changeConfig);
    }
  }

  /**
   * Processes the firmware.<br>
   * It searches in the database the firmware target for the charge point. We upgrade the firmware
   * step by step, meaning we choose the closest firmware to the current one.
   *
   * @return an optional of the message sent.
   */
  private Optional<OcppMessage> processFirmwareRequest() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    if (currentChargepoint.configuration() == null) {
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.PENDING));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      chargePointManager.notifyStatusUpdate();
      return Optional.empty();
    }
    var firmware = currentChargepoint.configuration().firmware();
    LOGGER.info(
        "firmware target ({}) for charge point {}",
        firmware.version(),
        currentChargepoint.serialNumber());
    var link = "";
    for (var typeAllowed : firmware.typesAllowed().typesAllowedSet()) {
      if (typeAllowed.type().equals(currentChargepoint.type())
          && typeAllowed.constructor().equals(currentChargepoint.constructor())) {
        link = fetchUrlFromFirstCompatibleVersion(typeAllowed);
        break;
      }
    }
    if (link.isEmpty()) {
      LOGGER.info(
          "firmware update for the charge point {} is done !", currentChargepoint.serialNumber());
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint.withStatus(Step.CONFIGURATION, StatusProcess.PENDING));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      // Dispatch information to users
      chargePointManager.notifyStatusUpdate();
      chargePointManager.notifyProcess();
      return processConfigurationRequest();
    }
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.PROCESSING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate();
    var firmwareRequest = new UpdateFirmwareRequest16(link, LocalDateTime.now().toString());
    sender.sendMessage(firmwareRequest, chargePointManager);
    return Optional.of(firmwareRequest);
  }

  /**
   * Fetches the first compatible firmware version from the database.
   *
   * @param typeAllowed the {@link TypeAllowed} matching the charge point.
   * @return the URL for downloading the firmware, an empty string if none is compatible.
   */
  private String fetchUrlFromFirstCompatibleVersion(TypeAllowed typeAllowed) {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    var comparison = targetFirmwareVersion.compareTo(firmwareVersion);
    if (comparison > 0) {
      var firmwares = firmwareRepository.findAllByTypeAllowedAsc(typeAllowed);
      for (var firmware : firmwares.firmwareList()) {
        if (firmware.version().compareTo(firmwareVersion) > 0) {
          LOGGER.info(
              "updating charge point {} with firmware {}",
              currentChargepoint.serialNumber(),
              firmware.version());
          return firmware.url();
        }
      }
      LOGGER.info(
          "didn't find any compatible firmware for charge point {} : skipping to CONFIGURATION",
          currentChargepoint.serialNumber());
      return "";
    } else if (comparison < 0) {
      LOGGER.info(
          "tried to rollback charge point {} : FORBIDDEN, skipping to CONFIGURATION",
          currentChargepoint.serialNumber());
      return "";
    }
    return "";
  }

  /**
   * Processes the received {@link ResetResponse16}.
   *
   * @return an empty optional.
   */
  private Optional<OcppMessage> processResetResponse() {
    if (chargePointManager.getCurrentChargepoint() != null) {
      var currentChargepoint = chargePointManager.getCurrentChargepoint();
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint
                  .withState(false)
                  .withStatus(currentChargepoint.step(), StatusProcess.FINISHED));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      // Dispatch information to users
      chargePointManager.notifyStatusUpdate();
    }
    return Optional.empty();
  }

  private Optional<OcppMessage> processResetRequest() {
    var reset = new ResetRequest16(ResetType.Hard);
    sender.sendMessage(reset, chargePointManager);
    return Optional.of(reset);
  }

  /**
   * Processes the current firmware installation status.<br>
   * If the download has failed or the installation has failed, we stop the process, otherwise we
   * continue as normal.
   *
   * @param f the {@link FirmwareStatusNotificationRequest16}.
   * @return an optional of the next message sent.
   */
  private Optional<OcppMessage> processFirmwareStatusResponse(
      FirmwareStatusNotificationRequest16 f) {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    // Acknowledge the notification (empty response).
    sender.sendMessage(new FirmwareStatusNotificationResponse16(), chargePointManager);
    return switch (f.status()) {
      case Installed -> {
        LOGGER.info(
            "charge point {} has successfully installed the firmware",
            currentChargepoint.serialNumber());
        currentChargepoint =
            chargePointRepository.save(
                currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.PENDING));
        chargePointManager.setCurrentChargepoint(currentChargepoint);
        var reset = new ResetRequest16(ResetType.Hard);
        sender.sendMessage(reset, chargePointManager);
        LOGGER.info("rebooting the charge point {}", currentChargepoint.serialNumber());
        yield Optional.of(reset);
      }
      case DownloadFailed, InstallationFailed -> {
        if (f.status() == FirmwareStatus.DownloadFailed) {
          LOGGER.warn(
              "firmware for the charge point {} couldn't be downloaded, "
                  + "check the internet connection of the charge point",
              currentChargepoint.serialNumber());
        } else {
          LOGGER.warn(
              "firmware for the charge point {} couldn't be installed, "
                  + "check the given URL to the charge point",
              currentChargepoint.serialNumber());
        }
        currentChargepoint =
            chargePointRepository.save(
                currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.FAILED));
        chargePointManager.setCurrentChargepoint(currentChargepoint);
        // Dispatch information to users
        chargePointManager.notifyStatusUpdate();
        chargePointManager.notifyProcess();
        yield Optional.empty();
      }
      default -> Optional.empty();
    };
  }

  /** Loads the configuration key-values of the current charge point into the waiting queue. */
  private void loadKeyValue() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    var configuration = currentChargepoint.configuration().configuration();
    try {
      HashMap<String, String> configMap =
          new ObjectMapper().readValue(configuration, new TypeReference<>() {});
      configMap.forEach(
          (key, value) -> {
            var configMessage =
                new ChangeConfigurationRequest16(
                    ConfigurationTranscriptor.idToEnum(Integer.parseInt(key))
                        .getOcpp16Key()
                        .getKeyAccordingToVersion(firmwareVersion),
                    value);
            queue.add(configMessage);
            LOGGER.info(
                "added configuration in the waiting list for the charge point {} : {}",
                currentChargepoint.serialNumber(),
                configMessage);
          });
    } catch (Exception e) {
      LOGGER.error(
          "couldn't read configuration for the charge point {}",
          currentChargepoint.serialNumber(),
          e);
    }
  }

  private Optional<OcppMessage> processDefaultMessage() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    if (currentChargepoint == null) {
      return processResetRequest();
    }
    var found =
        chargePointRepository.findBySerialNumberChargepointAndConstructor(
            currentChargepoint.serialNumber(), currentChargepoint.constructor());
    if (found.isEmpty()) {
      return Optional.empty();
    }
    currentChargepoint = found.orElseThrow();
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    var step = currentChargepoint.step();
    var status = currentChargepoint.statusProcess();
    if (step == Step.CONFIGURATION && status == StatusProcess.PENDING) {
      return processConfigurationRequest();
    } else if (status == StatusProcess.FAILED || status == StatusProcess.FINISHED) {
      loaded = false;
      lastOrderModeOn = false;
      return processFirmwareRequest();
    } else {
      return Optional.empty();
    }
  }
}
