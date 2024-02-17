package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageBuilder;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationRequest2;
import fr.uge.chargepointconfiguration.entities.Chargepoint;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Objects;
import java.util.Optional;

/**
 * Manages the charge point by listening and sending messages to the charge point.
 */
public class ChargePointManager {
  private final OcppVersion ocppVersion;
  private final OcppMessageParser ocppMessageParser;
  private final OcppMessageBuilder ocppMessageBuilder;
  private final MessageSender messageSender;
  private final ChargepointRepository chargepointRepository;

  /**
   * ChargePointManager's constructor.
   *
   * @param ocppVersion The version of the OCPP protocol (1.6 or 2.0.1).
   * @param messageSender The websocket connection used to send data.
   * @param chargepointRepository The chargepoint's repository for database queries.
   */
  public ChargePointManager(OcppVersion ocppVersion,
                            MessageSender messageSender,
                            ChargepointRepository chargepointRepository) {
    Objects.requireNonNull(ocppVersion);
    Objects.requireNonNull(messageSender);
    Objects.requireNonNull(chargepointRepository);
    this.ocppVersion = ocppVersion;
    this.ocppMessageParser = OcppMessageParser.instantiateFromVersion(ocppVersion);
    this.ocppMessageBuilder = OcppMessageBuilder.instantiateFromVersion(ocppVersion);
    this.messageSender = messageSender;
    this.chargepointRepository = chargepointRepository;
  }

  /**
   * Processes the received websocket message according to the OCPP protocol and
   * returns the response we've sent to the sender.<br>
   * According to the message, we send (or not) a message.<br>
   * For example :<br>
   * If the message is BootNotificationRequest, the response should be BootNotificationResponse.
   *
   * @param webSocketRequestMessage The websocket message sent to our server.
   * @return A String representing the response we've sent to the sender.
   */
  public Optional<WebSocketResponseMessage> processMessage(
          WebSocketRequestMessage webSocketRequestMessage) {
    Objects.requireNonNull(webSocketRequestMessage);
    var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
    // TODO : If it is a BootNotificationRequest, we should save the sender into the database.
    var resp = ocppMessageBuilder.buildMessage(webSocketRequestMessage);
    if (resp.isPresent()) {
      checkAndRegisterInDatabase(webSocketRequestMessage);
      var webSocketResponseMessage = new WebSocketResponseMessage(3,
              webSocketRequestMessage.messageId(),
              JsonParser.objectToJsonString(resp.orElseThrow()));
      messageSender.sendMessage(webSocketResponseMessage);
      return Optional.of(webSocketResponseMessage);
    }
    return Optional.empty();
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

  /**
   * Checks if the message is a BootNotificationRequest.<br>
   * If it is the case, this method will check in database if the sender has already been
   * saved into the database.<br>
   * If the sender was not in the database, we create a new entry.
   *
   * @param message The message sent by the sender with the message's type.
   */
  private void checkAndRegisterInDatabase(WebSocketRequestMessage message) {
    if (message.messageName()
            == WebSocketRequestMessage.WebSocketMessageName.BOOT_NOTIFICATION_REQUEST) {
      if (ocppVersion == OcppVersion.V1_6) {
        var bootNotification = JsonParser.stringToObject(
                BootNotificationRequest16.class, message.data()
        );
        var chargePointInDatabase =
                chargepointRepository.findBySerialNumber(
                        bootNotification.chargePointSerialNumber()
                );
        if (chargePointInDatabase == null) {
          // TODO : create and save into database
        }
      } else if (ocppVersion == OcppVersion.V2) {
        var bootNotification = JsonParser.stringToObject(
                BootNotificationRequest2.class, message.data()
        );
        // TODO : Check in DB if the current chargepoint does exist.
      }
    }
  }
}
