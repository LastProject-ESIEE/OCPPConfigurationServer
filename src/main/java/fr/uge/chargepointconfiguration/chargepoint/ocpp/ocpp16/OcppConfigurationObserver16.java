package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.WebSocketHandler;
import fr.uge.chargepointconfiguration.chargepoint.ChargePointManager;
import fr.uge.chargepointconfiguration.chargepoint.OcppMessageSender;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppObserver;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.RegistrationStatus;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationResponse20;
import fr.uge.chargepointconfiguration.entities.Chargepoint;
import fr.uge.chargepointconfiguration.entities.Status;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import fr.uge.chargepointconfiguration.repository.StatusRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Defines the OCPP configuration message for the visitor.
 */
public class OcppConfigurationObserver16 implements OcppObserver {
  private final OcppMessageSender sender;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;
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
      case ChangeConfigurationResponse16 c -> System.out.println(
              "Response change configuration : " + c.status()
      );
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
    // TODO : Add log,we sent a bootNotif response
    if (status.getStep() == Status.Step.CONFIGURATION) {
      status = currentChargepoint.getStatus();
      status.setStatus(Status.StatusProcess.PROCESSING);
      status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
      chargepointRepository.save(currentChargepoint);
      testConfiguration(chargePointManager);
    }
  }

  private void processChangeConfiguration(ChangeConfigurationResponse16 changeConfiguration,
                                          ChargePointManager chargePointManager) {
    var status = currentChargepoint.getStatus();
    status.setStatus(Status.StatusProcess.FINISHED);
    status.setError(changeConfiguration.status());
    status.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    chargepointRepository.save(currentChargepoint);
  }

  private void testConfiguration(ChargePointManager chargePointManager) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      //skip
    }
    var updateLightRequest = new ChangeConfigurationRequest16("LightIntensity", "100");
    sender.sendMessage(updateLightRequest, chargePointManager);
  }
}
