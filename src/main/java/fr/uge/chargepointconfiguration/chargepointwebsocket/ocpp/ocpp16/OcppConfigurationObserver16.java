package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepointwebsocket.OcppMessageSender;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppObserver;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.RegistrationStatus;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.BootNotificationResponse20;
import fr.uge.chargepointconfiguration.configuration.ConfigurationTranscriptor;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.logs.CustomLogger;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLog;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLogEntity;
import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLogEntity;
import fr.uge.chargepointconfiguration.status.Status;
import fr.uge.chargepointconfiguration.status.StatusRepository;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * Defines the OCPP configuration message for the visitor.
 */
public class OcppConfigurationObserver16 implements OcppObserver {
  private final OcppMessageSender sender;
  private final ChargePointManager chargePointManager;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;
  private final Queue<ChangeConfigurationRequest16> queue = new LinkedList<>();
  private final CustomLogger logger;
  private String firmwareVersion;
  private String targetFirmwareVersion;


  /**
   * Constructor for the OCPP 1.6 configuration observer.
   *
   * @param sender                websocket channel to send message
   * @param chargepointRepository charge point repository
   * @param firmwareRepository    firmware repository
   * @param statusRepository      charge point status repository
   */
  public OcppConfigurationObserver16(OcppMessageSender sender,
                                     ChargePointManager chargePointManager,
                                     ChargepointRepository chargepointRepository,
                                     FirmwareRepository firmwareRepository,
                                     StatusRepository statusRepository,
                                     CustomLogger logger) {
    this.sender = sender;
    this.chargePointManager = Objects.requireNonNull(chargePointManager);
    this.chargepointRepository = chargepointRepository;
    this.firmwareRepository = firmwareRepository;
    this.statusRepository = statusRepository;
    this.logger = Objects.requireNonNull(logger);
  }

  @Override
  public void onMessage(OcppMessage ocppMessage) {
    switch (ocppMessage) {
      case BootNotificationRequest16 b -> processBootNotification(b);
      case ChangeConfigurationResponse16 c -> processConfigurationResponse(c);
      case ResetResponse16 r -> processResetResponse();
      case FirmwareStatusNotificationRequest16 f -> processFirmwareStatusResponse(f);
      default -> {
        // Do nothing
      }
    }
  }

  @Override
  public void onConnection(ChargePointManager chargePointManager) {

  }

  @Override
  public void onDisconnection(ChargePointManager chargePointManager) {

  }

  private void processBootNotification(
          BootNotificationRequest16 bootNotificationRequest16) {
    firmwareVersion = bootNotificationRequest16.firmwareVersion();
    // Get charge point from database
    chargePointManager.setCurrentChargepoint(
            chargepointRepository.findBySerialNumberChargepointAndConstructor(
                    bootNotificationRequest16.chargePointSerialNumber(),
                    bootNotificationRequest16.chargePointVendor()
            ));
    // If charge point is not found then skip it
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    if (currentChargepoint == null) {
      logger.warn(new TechnicalLog(TechnicalLogEntity.Component.BACKEND,
              "unknown chargepoint, serial number : "
                      + bootNotificationRequest16.chargePointSerialNumber()));
      var response = new BootNotificationResponse20(
              LocalDateTime.now().toString(),
              10,
              RegistrationStatus.Rejected
      );
      sender.sendMessage(response, chargePointManager);
      return;
    }
    var status = currentChargepoint.getStatus();
    var config = currentChargepoint.getConfiguration();
    targetFirmwareVersion = config.getFirmware().getVersion();
    status.setState(true);
    status.setStatus(Status.StatusProcess.PENDING);
    var statusLastTime = status.getLastUpdate();
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    logger.info(new BusinessLog(null,
            currentChargepoint,
            BusinessLogEntity.Category.LOGIN,
            "chargepoint ("
                    + currentChargepoint.getSerialNumberChargepoint()
                    + ") is authenticated"));
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
    // Send BootNotification Response
    var response = new BootNotificationResponse16(
            LocalDateTime.now().toString(),
            5,
            RegistrationStatus.Accepted
    );
    sender.sendMessage(response, chargePointManager);
    switch (status.getStep()) {
      case Status.Step.CONFIGURATION -> processConfigurationRequest();
      case Status.Step.FIRMWARE -> processFirmwareRequest();
      default -> {
        // ignore
      }
    }
  }

  private void processConfigurationRequest() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    if (queue.isEmpty()) {
      // The change configuration list is empty, so we load the configuration
      var configuration = currentChargepoint.getConfiguration().getConfiguration();
      var mapper = new ObjectMapper();
      HashMap<String, String> configMap;
      try {
        configMap = mapper.readValue(configuration, HashMap.class);
      } catch (JsonProcessingException e) {
        logger.error(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.CONFIG,
                "couldn't read configuration for the chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ")"));
        return;
      }
      for (Map.Entry<String, String> set :
              configMap.entrySet()) {
        var config = new ChangeConfigurationRequest16(
                ConfigurationTranscriptor.idToEnum(Integer.parseInt(set.getKey())).getOcpp16Key(),
                set.getValue());
        queue.add(config);
        logger.info(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.CONFIG,
                "added configuration in the waiting list for the chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ") : "
                        + config));
      }
    }
    var config = queue.poll();
    if (config == null) {
      logger.info(new BusinessLog(null,
              currentChargepoint,
              BusinessLogEntity.Category.CONFIG,
              "configuration for the chargepoint ("
                      + currentChargepoint.getSerialNumberChargepoint()
                      + ") is done ! "));
      var status = currentChargepoint.getStatus();
      status.setStatus(Status.StatusProcess.FINISHED);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      chargepointRepository.save(currentChargepoint);
      // Dispatch information to users
      chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
    } else {
      sender.sendMessage(config, chargePointManager);
      var status = currentChargepoint.getStatus();
      status.setStatus(Status.StatusProcess.PROCESSING);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      chargepointRepository.save(currentChargepoint);
      // Dispatch information to users
      chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
    }
  }

  private void processConfigurationResponse(ChangeConfigurationResponse16 response) {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    switch (response.status()) {
      case "Accepted", "RebootRequired" -> {
        if (queue.isEmpty()) {
          var status = currentChargepoint.getStatus();
          status.setStatus(Status.StatusProcess.FINISHED);
          status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
          currentChargepoint.setStatus(status);
          chargepointRepository.save(currentChargepoint);
          // Dispatch information to users
          chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
          var reset = new ResetRequest16("Hard");
          sender.sendMessage(reset, chargePointManager);
          logger.info(new BusinessLog(null,
                  currentChargepoint,
                  BusinessLogEntity.Category.CONFIG,
                  "configuration for the chargepoint ("
                          + currentChargepoint.getSerialNumberChargepoint()
                          + ") is done ! "));
        } else {
          processConfigurationRequest();
        }
      }
      default -> {
        logger.warn(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.CONFIG,
                "configuration for the chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ") has failed, see its status ! "));
        var status = currentChargepoint.getStatus();
        status.setStatus(Status.StatusProcess.FAILED);
        status.setError(response.status());
        currentChargepoint.setStatus(status);
        chargepointRepository.save(currentChargepoint);
        // Dispatch information to users
        chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
        queue.clear();
      }
    }
  }

  private void processFirmwareRequest() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    var status = currentChargepoint.getStatus();
    var firmware = currentChargepoint.getConfiguration().getFirmware();
    logger.info(new BusinessLog(null,
            currentChargepoint,
            BusinessLogEntity.Category.FIRM,
            "firmware target ("
                    + firmware.getVersion()
                    + ") for chargepoint ( "
                    + currentChargepoint.getSerialNumberChargepoint()
                    + ")"));
    var typesAllowed = firmware.getTypesAllowed();
    var link = "";
    for (var typeAllowed : typesAllowed) {
      if (typeAllowed.getType().equals(currentChargepoint.getType())
              && typeAllowed.getConstructor().equals(currentChargepoint.getConstructor())) {
        link = fetchUrlFromFirstCompatibleVersion(typeAllowed);
        break;
      }
    }
    if (link.isEmpty()) {
      logger.info(new BusinessLog(null,
              currentChargepoint,
              BusinessLogEntity.Category.FIRM,
              "firmware update for the chargepoint ("
                      + currentChargepoint.getSerialNumberChargepoint()
                      + ") is done ! "));
      status.setStatus(Status.StatusProcess.PENDING);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      status.setStep(Status.Step.CONFIGURATION);
      currentChargepoint.setStatus(status);
      chargepointRepository.save(currentChargepoint);
      // Dispatch information to users
      chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
      processConfigurationRequest();
      return;
    }
    status.setStatus(Status.StatusProcess.PROCESSING);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
    var firmwareRequest = new UpdateFirmwareRequest16(
            link, LocalDateTime.now().toString());
    sender.sendMessage(firmwareRequest, chargePointManager);
  }

  private String fetchUrlFromFirstCompatibleVersion(TypeAllowed typeAllowed) {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    var firmwares = firmwareRepository
            .findAllByTypeAllowed(typeAllowed);
    for (var firmware : firmwares) {
      var comparison = targetFirmwareVersion.compareTo(firmwareVersion);
      if (comparison > 0) {
        if (firmware.getVersion().compareTo(firmwareVersion) > 0) {
          logger.info(new BusinessLog(null,
                  currentChargepoint,
                  BusinessLogEntity.Category.FIRM,
                  "updating chargepoint ("
                          + currentChargepoint.getSerialNumberChargepoint()
                          + ") with firmware "
                          + firmware.getVersion()));
          return firmware.getUrl();
        }
      } else if (comparison == 0) {
        logger.info(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.FIRM,
                "chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ") is already updated"));
        return "";
      } else {
        logger.warn(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.FIRM,
                "cannot downgrade chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ") (CURRENT : "
                        + firmwareVersion
                        + ", TARGET : "
                        + firmware.getVersion()
                        + ")"));
        return "";
      }
    }
    logger.warn(new BusinessLog(null,
            currentChargepoint,
            BusinessLogEntity.Category.FIRM,
            "cannot find compatible firmware for chargepoint ("
                    + currentChargepoint.getSerialNumberChargepoint()
                    + "), SKIPPING THIS PROCESS"));
    return "";
  }

  private void processResetResponse() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    var status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.FINISHED);
    status.setState(false);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
  }

  private void processFirmwareStatusResponse(FirmwareStatusNotificationRequest16 f) {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    switch (f.status()) {
      case "Installed" -> {
        logger.info(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.FIRM,
                "chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ") has successfully installed the firmware"));
        var status = currentChargepoint.getStatus();
        status.setStatus(Status.StatusProcess.PENDING);
        status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        currentChargepoint.setStatus(status);
        chargepointRepository.save(currentChargepoint);
        var reset = new ResetRequest16("Hard");
        sender.sendMessage(reset, chargePointManager);
        logger.info(new BusinessLog(null,
                currentChargepoint,
                BusinessLogEntity.Category.FIRM,
                "rebooting the chargepoint ("
                        + currentChargepoint.getSerialNumberChargepoint()
                        + ")"));
      }
      case "DownloadFailed",
              "InstallationFailed" -> {
        if (f.status().equals("DownloadFailed")) {
          logger.warn(new BusinessLog(null,
                  currentChargepoint,
                  BusinessLogEntity.Category.FIRM,
                  "firmware for the chargepoint ("
                          + currentChargepoint.getSerialNumberChargepoint()
                          + ") couldn't be downloaded, "
                          + "check the internet connection of the chargepoint, SKIPPING"));
        } else {
          logger.warn(new BusinessLog(null,
                  currentChargepoint,
                  BusinessLogEntity.Category.FIRM,
                  "firmware for the chargepoint ("
                          + currentChargepoint.getSerialNumberChargepoint()
                          + ") couldn't be installed, check the given URL to the chargepoint, "
                          + "SKIPPING"));
        }
        var status = currentChargepoint.getStatus();
        //status.setStatus(Status.StatusProcess.FAILED);
        //status.setError(f.status());
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          // ignore
        }
        status.setStatus(Status.StatusProcess.PENDING);
        status.setStep(Status.Step.CONFIGURATION);
        status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        currentChargepoint.setStatus(status);
        chargePointManager.notifyStatusUpdate(currentChargepoint.getId(), status);
        chargepointRepository.save(currentChargepoint);
        var reset = new ResetRequest16("Hard");
        sender.sendMessage(reset, chargePointManager);
      }
      default -> {
        // Ignore, the chargepoint is downloading/installing.
      }
    }
  }
}
