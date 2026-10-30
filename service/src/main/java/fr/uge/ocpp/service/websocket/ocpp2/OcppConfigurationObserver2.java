package fr.uge.ocpp.service.websocket.ocpp2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.service.websocket.ChargePointManager;
import fr.uge.ocpp.service.websocket.OcppMessageSender;
import fr.uge.ocpp.service.websocket.OcppObserver;
import fr.uge.ocpp.websocket.ocpp.OcppMessage;
import fr.uge.ocpp.websocket.ocpp.ocpp16.data.RegistrationStatus;
import fr.uge.ocpp.websocket.ocpp.ocpp2.BootNotificationRequest20;
import fr.uge.ocpp.websocket.ocpp.ocpp2.BootNotificationResponse20;
import fr.uge.ocpp.websocket.ocpp.ocpp2.SetVariablesRequest20;
import fr.uge.ocpp.websocket.ocpp.ocpp2.SetVariablesResponse20;
import fr.uge.ocpp.websocket.ocpp.ocpp2.data.Component;
import fr.uge.ocpp.websocket.ocpp.ocpp2.data.SetVariableData;
import fr.uge.ocpp.websocket.ocpp.ocpp2.data.VariableType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the OCPP 2.0.1 observer.<br>
 * An observer is defined by the interface {@link OcppObserver}.<br>
 * Note: the firmware update is not implemented for OCPP 2.0.1 yet (see
 * doc/serverChargepointCommunicationDoc.md), the process goes straight to the configuration step.
 */
public class OcppConfigurationObserver2 implements OcppObserver {
  private static final Logger LOGGER = LoggerFactory.getLogger(OcppConfigurationObserver2.class);

  private final OcppMessageSender sender;
  private final ChargePointManager chargePointManager;
  private final ChargePointRepository chargePointRepository;
  private final FirmwareRepository firmwareRepository;
  private final Queue<SetVariableData> queue = new LinkedList<>();

  /**
   * {@link OcppConfigurationObserver2}'s constructor.
   *
   * @param sender the web socket sender.
   * @param chargePointManager the charge point's manager.
   * @param chargePointRepository the charge point repository.
   * @param firmwareRepository the firmware repository.
   */
  public OcppConfigurationObserver2(
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
      return Optional.empty();
    }
    return switch (ocppMessage) {
      case BootNotificationRequest20 b -> processBootNotification(b);
      case SetVariablesResponse20 r -> processConfigurationResponse(r);
      default -> Optional.empty();
    };
  }

  /**
   * Processes the received {@link BootNotificationRequest20} sent by the charge point.
   *
   * @param bootNotificationRequest the {@link BootNotificationRequest20}.
   * @return an optional of the response sent.
   */
  private Optional<OcppMessage> processBootNotification(
      BootNotificationRequest20 bootNotificationRequest) {
    // Get charge point from database
    var found =
        chargePointRepository.findBySerialNumberChargepointAndConstructor(
            bootNotificationRequest.chargingStation().serialNumber(),
            bootNotificationRequest.chargingStation().vendorName());
    // If charge point is not found then skip it
    if (found.isEmpty()) {
      var response =
          new BootNotificationResponse20(
              LocalDateTime.now().toString(), 5, RegistrationStatus.Rejected);
      sender.sendMessage(response, chargePointManager);
      return Optional.of(response);
    }
    var currentChargepoint = found.orElseThrow();
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint
                .withState(true)
                .withStatus(currentChargepoint.step(), StatusProcess.PENDING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    // Dispatch information to users
    chargePointManager.notifyStatusUpdate();
    // Send BootNotification Response
    var response =
        new BootNotificationResponse20(
            LocalDateTime.now().toString(), 5, RegistrationStatus.Accepted);
    sender.sendMessage(response, chargePointManager);
    return switch (currentChargepoint.step()) {
      case Step.CONFIGURATION -> processConfigurationRequest();
      case Step.FIRMWARE -> processFirmwareRequest();
    };
  }

  /**
   * Processes the configuration: loads the key-values of the charge point's configuration and sends
   * them in a single {@link SetVariablesRequest20}.
   *
   * @return an optional of the message sent.
   */
  private Optional<OcppMessage> processConfigurationRequest() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    var configuration = currentChargepoint.configuration().configuration();
    HashMap<String, HashMap<String, String>> configMap;
    try {
      configMap = new ObjectMapper().readValue(configuration, new TypeReference<>() {});
    } catch (Exception e) {
      LOGGER.error(
          "couldn't read configuration for the charge point {}",
          currentChargepoint.serialNumber(),
          e);
      return Optional.empty();
    }
    for (var component : configMap.keySet()) {
      var componentConfig = configMap.get(component);
      for (var key : componentConfig.keySet()) {
        var value = componentConfig.get(key);
        queue.add(new SetVariableData(value, new Component(component), new VariableType(key)));
      }
    }
    if (queue.isEmpty()) {
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.FINISHED));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      chargePointManager.notifyStatusUpdate();
      return Optional.empty();
    }
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint
                .withState(true)
                .withStatus(currentChargepoint.step(), StatusProcess.PROCESSING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    chargePointManager.notifyStatusUpdate();
    var setVariableList = new ArrayList<SetVariableData>();
    while (!queue.isEmpty()) {
      setVariableList.add(queue.poll());
    }
    var setVariableRequest = new SetVariablesRequest20(setVariableList);
    sender.sendMessage(setVariableRequest, chargePointManager);
    return Optional.of(setVariableRequest);
  }

  /**
   * Processes the firmware step.<br>
   * TODO: send an UpdateFirmware request, for now the process goes straight to the configuration
   * step.
   *
   * @return an optional of the message sent.
   */
  private Optional<OcppMessage> processFirmwareRequest() {
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.PROCESSING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    chargePointManager.notifyStatusUpdate();
    // TODO : Send an UpdateFirmware request !
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint.withStatus(Step.CONFIGURATION, StatusProcess.PENDING));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    chargePointManager.notifyStatusUpdate();
    return processConfigurationRequest();
  }

  /**
   * Processes the {@link SetVariablesResponse20} sent by the charge point.<br>
   * If any variable has been rejected, the process is marked as FAILED, otherwise FINISHED.
   *
   * @param response the {@link SetVariablesResponse20}.
   * @return an empty optional.
   */
  private Optional<OcppMessage> processConfigurationResponse(SetVariablesResponse20 response) {
    var noFailure = true;
    var failedConfig = new StringBuilder();
    for (var result : response.setVariableResult()) {
      if (!result.attributeStatus().equals("Accepted")) {
        noFailure = false;
        failedConfig
            .append(result.attributeStatus())
            .append(" :\n\tComponent : ")
            .append(result.component().name())
            .append("\n\tVariable : ")
            .append(result.variable().name())
            .append("\n");
      }
    }
    var currentChargepoint = chargePointManager.getCurrentChargepoint();
    if (!noFailure) {
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint
                  .withError(failedConfig.toString())
                  .withStatus(currentChargepoint.step(), StatusProcess.FAILED));
      chargePointManager.setCurrentChargepoint(currentChargepoint);
      chargePointManager.notifyStatusUpdate();
      return Optional.empty();
    }
    currentChargepoint =
        chargePointRepository.save(
            currentChargepoint.withStatus(currentChargepoint.step(), StatusProcess.FINISHED));
    chargePointManager.setCurrentChargepoint(currentChargepoint);
    chargePointManager.notifyStatusUpdate();
    return Optional.empty();
  }
}
