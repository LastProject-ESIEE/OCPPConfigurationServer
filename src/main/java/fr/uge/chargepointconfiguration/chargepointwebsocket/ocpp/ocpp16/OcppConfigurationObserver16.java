package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.chargepointconfiguration.WebSocketHandler;
import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepointwebsocket.OcppMessageSender;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppObserver;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.RegistrationStatus;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.BootNotificationResponse20;
import fr.uge.chargepointconfiguration.configuration.ConfigurationTranscriptor;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.status.Status;
import fr.uge.chargepointconfiguration.status.StatusRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Defines the OCPP configuration message for the visitor.
 */
public class OcppConfigurationObserver16 implements OcppObserver {
  private final OcppMessageSender sender;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;
  private final Queue<ChangeConfigurationRequest16> queue = new LinkedList<>();
  private Chargepoint currentChargepoint;


  /**
   * Constructor for the OCPP 1.6 configuration observer.
   *
   * @param sender                websocket channel to send message
   * @param chargepointRepository charge point repository
   * @param firmwareRepository    firmware repository
   * @param statusRepository      charge point status repository
   */
  public OcppConfigurationObserver16(OcppMessageSender sender,
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
      case BootNotificationRequest16 b -> processBootNotification(b, chargePointManager);
      case ChangeConfigurationResponse16 c -> processConfigurationResponse(c, chargePointManager);
      case ResetResponse16 r -> processResetResponse();
      // TODO : Add switch case for the update firmware response and status firmware request.
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
          BootNotificationRequest16 bootNotificationRequest16,
          ChargePointManager chargePointManager) {
    // Get charge point from database
    currentChargepoint = chargepointRepository.findBySerialNumberChargepointAndConstructor(
            bootNotificationRequest16.chargePointSerialNumber(),
            bootNotificationRequest16.chargePointVendor()
    );
    // If charge point is not found then skip it
    if (currentChargepoint == null) {
      // TODO : Add log, this chargepoint is unknown
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
    status.setState(true);
    status.setStatus(Status.StatusProcess.PENDING);
    var statusLastTime = status.getLastUpdate();
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    // TODO : Add log, the chargepoint is now connected
    chargepointRepository.save(currentChargepoint);
    // Dispatch information to users
    WebSocketHandler.sendMessageToUsers("Charge point x connected !");
    // Send BootNotification Response
    var response = new BootNotificationResponse16(
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
    if (queue.isEmpty()) {
      // The change configuration list is empty, so we load the configuration
      var configuration = currentChargepoint.getConfiguration().getConfiguration();
      var mapper = new ObjectMapper();
      HashMap<String, String> configMap;
      try {
        configMap = mapper.readValue(configuration, HashMap.class);
      } catch (JsonProcessingException e) {
        return;
      }
      for (Map.Entry<String, String> set :
              configMap.entrySet()) {
        var config = new ChangeConfigurationRequest16(
                ConfigurationTranscriptor.idToEnum(Integer.parseInt(set.getKey())).getOcpp16Key(),
                set.getValue());
        queue.add(config);
      }
    }
    var config = queue.poll();
    if (config == null) {
      var status = currentChargepoint.getStatus();
      status.setStatus(Status.StatusProcess.FINISHED);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      chargepointRepository.save(currentChargepoint);
    } else {
      sender.sendMessage(config, chargePointManager);
      var status = currentChargepoint.getStatus();
      status.setStatus(Status.StatusProcess.PROCESSING);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      chargepointRepository.save(currentChargepoint);
    }
  }

  private void processConfigurationResponse(ChangeConfigurationResponse16 response,
                                            ChargePointManager chargePointManager) {
    switch (response.status()) {
      case "Accepted", "RebootRequired" -> {
        if (queue.isEmpty()) {
          var status = currentChargepoint.getStatus();
          status.setStatus(Status.StatusProcess.FINISHED);
          status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
          currentChargepoint.setStatus(status);
          chargepointRepository.save(currentChargepoint);
          var reset = new ResetRequest16("Hard");
          sender.sendMessage(reset, chargePointManager);
        } else {
          processConfigurationRequest(chargePointManager);
        }
      }
      default -> {
        var status = currentChargepoint.getStatus();
        status.setStatus(Status.StatusProcess.FAILED);
        status.setError(response.status());
        currentChargepoint.setStatus(status);
        chargepointRepository.save(currentChargepoint);
        queue.clear();
      }
    }
  }

  private void processFirmwareRequest(ChargePointManager chargePointManager) {
    var status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.PROCESSING);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    currentChargepoint.setStatus(status);
    chargepointRepository.save(currentChargepoint);
    // TODO : Send a update firmware request !
    status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.PENDING);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    status.setStep(Status.Step.CONFIGURATION);
    currentChargepoint.setStatus(status);
    chargepointRepository.save(currentChargepoint);
    processConfigurationRequest(chargePointManager);
  }

  private void processResetResponse() {
    var status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.FINISHED);
    status.setState(false);
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    chargepointRepository.save(currentChargepoint);
  }
}
