package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.chargepointconfiguration.WebSocketHandler;
import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.chargepoint.notification.ChargePointWebsocketNotification;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepointwebsocket.OcppMessageSender;
import fr.uge.chargepointconfiguration.chargepointwebsocket.WebSocketMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.MessageType;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppObserver;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.RegistrationStatus;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.data.Component;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.data.SetVariableData;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.data.VariableType;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.status.Status;
import fr.uge.chargepointconfiguration.status.StatusRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Defines the OCPP configuration message for the visitor.
 */
public class OcppConfigurationObserver2 implements OcppObserver {
  private final OcppMessageSender sender;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;
  private final Queue<SetVariableData> queue = new LinkedList<>();
  private Chargepoint currentChargepoint;

  /**
   * Constructor for the OCPP 2.0 configuration observer.
   *
   * @param sender                websocket channel to send message
   * @param chargepointRepository charge point repository
   * @param firmwareRepository    firmware repository
   * @param statusRepository      charge point status repository
   */
  public OcppConfigurationObserver2(OcppMessageSender sender,
                                    ChargepointRepository chargepointRepository,
                                    FirmwareRepository firmwareRepository,
                                    StatusRepository statusRepository) {
    this.sender = sender;
    this.chargepointRepository = chargepointRepository;
    this.firmwareRepository = firmwareRepository;
    this.statusRepository = statusRepository;
  }

  @Override
  public void onMessage(OcppMessage ocppMessage,
                        ChargePointManager chargePointManager) {
    switch (ocppMessage) {
      case BootNotificationRequest20 b -> processBootNotification(b, chargePointManager);
      case SetVariablesResponse20 r -> processConfigurationResponse(r, chargePointManager);
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

  private void notifyStatusUpdate(int id, Status status) {
    WebSocketHandler.sendMessageToUsers(
            new ChargePointWebsocketNotification(id, status)
    );
  }

  private void processBootNotification(
          BootNotificationRequest20 bootNotificationRequest,
          ChargePointManager chargePointManager) {

    // Get charge point from database
    currentChargepoint = chargepointRepository.findBySerialNumberChargepointAndConstructor(
            bootNotificationRequest.chargingStation().serialNumber(),
            bootNotificationRequest.chargingStation().vendorName()
    );
    // If charge point is not found then skip it
    if (currentChargepoint == null) {
      var response = new BootNotificationResponse20(
              LocalDateTime.now().toString(),
              5,
              RegistrationStatus.Rejected
      );
      sender.sendMessage(response, chargePointManager);
      return;
    }
    var status = currentChargepoint.getStatus();
    status.setState(true);
    status.setStatus(Status.StatusProcess.PENDING);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    notifyStatusUpdate(currentChargepoint.getId(), status);
    // Send BootNotification Response
    var response = new BootNotificationResponse20(
            LocalDateTime.now().toString(),
            5,
            RegistrationStatus.Accepted
    );
    sender.sendMessage(response, chargePointManager);
    switch (status.getStep()) {
      case Status.Step.CONFIGURATION -> processConfigurationRequest(chargePointManager);
      case Status.Step.FIRMWARE -> processFirmwareRequest(chargePointManager);
      default -> {
        // ignore
      }
    }
  }

  private void processConfigurationRequest(ChargePointManager chargePointManager) {
    var configuration = currentChargepoint.getConfiguration().getConfiguration();
    var mapper = new ObjectMapper();
    HashMap<String, HashMap<String, String>> configMap;
    try {
      configMap = mapper.readValue(configuration, HashMap.class);
    } catch (JsonProcessingException e) {
      return;
    }
    for (var component : configMap.keySet()) {
      var componentConfig = configMap.get(component);
      for (var key : componentConfig.keySet()) {
        var value = componentConfig.get(key);
        queue.add(new SetVariableData(
                value,
                new Component(component),
                new VariableType(key)
        ));
      }
    }
    var status = currentChargepoint.getStatus();
    if (queue.isEmpty()) {
      status.setStatus(Status.StatusProcess.FINISHED);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      currentChargepoint.setStatus(status);
      chargepointRepository.save(currentChargepoint);
      // Dispatch information to users
      notifyStatusUpdate(currentChargepoint.getId(), status);
    } else {
      status.setState(true);
      status.setStatus(Status.StatusProcess.PENDING);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      currentChargepoint.setStatus(status);
      chargepointRepository.save(currentChargepoint);
      // Dispatch information to users
      notifyStatusUpdate(currentChargepoint.getId(), status);
      var setVariableList = new ArrayList<SetVariableData>();
      while (!queue.isEmpty()) {
        setVariableList.add(queue.poll());
      }
      var setVariableRequest = new SetVariablesRequest20(setVariableList);
      sender.sendMessage(setVariableRequest, chargePointManager);
      chargePointManager.setPendingRequest(
              new WebSocketRequestMessage(MessageType.REQUEST.getCallType(),
                      chargePointManager.getCurrentId(),
                      WebSocketMessage.MessageTypeRequest.SET_VARIABLES_REQUEST,
                      JsonParser.objectToJsonString(setVariableRequest))
      );
    }
  }

  private void processFirmwareRequest(ChargePointManager chargePointManager) {
    var status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.PROCESSING);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    // Dispatch information to users
    notifyStatusUpdate(currentChargepoint.getId(), status);
    chargepointRepository.save(currentChargepoint);
    // TODO : Send a update firmware request !
    status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.PENDING);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    status.setStep(Status.Step.CONFIGURATION);
    currentChargepoint.setStatus(status);
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    notifyStatusUpdate(currentChargepoint.getId(), status);
    processConfigurationRequest(chargePointManager);
  }

  private void processConfigurationResponse(SetVariablesResponse20 response,
                                            ChargePointManager chargePointManager) {
    var noFailure = true;
    var failedConfig = new StringBuilder();
    for (var result : response.setVariableResult()) {
      if (!result.attributeStatus().equals("Accepted")) {
        noFailure = false;
        failedConfig.append(result.attributeStatus())
                .append(" :\n\tComponent : ")
                .append(result.component().name())
                .append("\n\tVariable : ")
                .append(result.variable().name())
                .append("\n");
      }
    }
    var status = currentChargepoint.getStatus();
    if (!noFailure) {
      status.setStatus(Status.StatusProcess.FAILED);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      status.setError(failedConfig.toString());
      currentChargepoint.setStatus(status);
      chargepointRepository.save(currentChargepoint);
      // Dispatch information to users
      notifyStatusUpdate(currentChargepoint.getId(), status);
      return;
    }
    status.setStatus(Status.StatusProcess.FINISHED);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    notifyStatusUpdate(currentChargepoint.getId(), status);
  }
}
