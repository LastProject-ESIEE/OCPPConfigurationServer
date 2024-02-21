package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppObserver;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationRequest20;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import fr.uge.chargepointconfiguration.repository.StatusRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Objects;

/**
 * Manages the charge point by listening and sending messages to the charge point.
 */
public class ChargePointManager {
  private final OcppVersion ocppVersion;
  private final OcppMessageParser ocppMessageParser;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;
  private final OcppObserver ocppObserver;
  private boolean authenticated = false;
  private long currentId = 1;

  /**
   * ChargePointManager's constructor.
   *
   * @param ocppVersion           The version of the OCPP protocol (1.6 or 2.0.1).
   * @param ocppMessageSender     The websocket connection used to send data.
   * @param chargepointRepository The chargepoint's repository for database queries.
   */
  public ChargePointManager(OcppVersion ocppVersion,
                            OcppMessageSender ocppMessageSender,
                            ChargepointRepository chargepointRepository,
                            FirmwareRepository firmwareRepository,
                            StatusRepository statusRepository) {
    this.ocppVersion = Objects.requireNonNull(ocppVersion);
    this.ocppMessageParser = OcppMessageParser.instantiateFromVersion(ocppVersion);
    this.chargepointRepository = Objects.requireNonNull(chargepointRepository);
    this.firmwareRepository = Objects.requireNonNull(firmwareRepository);
    this.statusRepository = Objects.requireNonNull(statusRepository);
    this.ocppObserver = OcppObserver.instantiateFromVersion(ocppVersion,
            ocppMessageSender,
            chargepointRepository,
            firmwareRepository,
            statusRepository
    );
  }

  /**
   * Returns the current websocket message id.
   *
   * @return A long representing the current websocket
   */
  public long getCurrentId() {
    return currentId;
  }

  /**
   * Processes the received websocket message according to the OCPP protocol and
   * returns the response we've sent to the sender.<br>
   * According to the message, we send (or not) a message.<br>
   * For example :<br>
   * If the message is BootNotificationRequest, the response should be BootNotificationResponse.
   *
   * @param webSocketRequestMessage The websocket message sent to our server.
   */
  public void processMessage(WebSocketRequestMessage webSocketRequestMessage) {
    Objects.requireNonNull(webSocketRequestMessage);
    var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
    if (message.isEmpty()) {
      return;
    }
    var ocppMessage = message.orElseThrow();
    switch (OcppMessage.ocppMessageToMessageType(ocppMessage)) {
      case RESPONSE -> currentId += 1;
      case REQUEST -> currentId = webSocketRequestMessage.messageId();
      default -> {
        // Weird message, ignore it.
      }
    }
    ocppObserver.onMessage(message.orElseThrow(), this);
  }

  /**
   * Does something when the sender has been disconnected.
   */
  public void onDisconnection() {
    // TODO change borne status
  }

  /**
   * Does something if there is an error.
   */
  public void onError() {
    // TODO change borne status
  }

  private boolean doesChargepointExistInDatabase(WebSocketRequestMessage message) {
    if (message.messageName()
        == WebSocketRequestMessage.WebSocketMessageName.BOOT_NOTIFICATION_REQUEST) {
      return switch (ocppVersion) {
        case V2 -> {
          var bootNotification = JsonParser.stringToObject(
                  BootNotificationRequest20.class, message.data()
          );
          var chargingStation = bootNotification.chargingStation();
          var chargePointInDatabase =
                  chargepointRepository.findBySerialNumberChargepointAndConstructor(
                          chargingStation.serialNumber(),
                          chargingStation.vendorName()
                  );
          yield chargePointInDatabase != null;
        }
        case V1_6 -> {
          var bootNotification = JsonParser.stringToObject(
                  BootNotificationRequest16.class, message.data()
          );
          var chargePointInDatabase =
                  chargepointRepository.findBySerialNumberChargepointAndConstructor(
                          bootNotification.chargePointSerialNumber(),
                          bootNotification.chargePointVendor()
                  );
          yield chargePointInDatabase != null;
        }
      };
    } else {
      return authenticated;
    }
  }
}
