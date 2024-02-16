package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageBuilder;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Objects;

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
  public WebSocketResponseMessage processMessage(WebSocketRequestMessage webSocketRequestMessage) {
    Objects.requireNonNull(webSocketRequestMessage);
    var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
    // TODO : If it is a BootNotificationRequest, we should save the sender into the database.
    var resp = ocppMessageBuilder.buildMessage(webSocketRequestMessage);
    var webSocketResponseMessage = new WebSocketResponseMessage(3,
            webSocketRequestMessage.messageId(),
            JsonParser.objectToJsonString(resp));
    messageSender.sendMessage(webSocketResponseMessage);
    return webSocketResponseMessage;
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
}
