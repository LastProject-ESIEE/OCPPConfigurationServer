package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageBuilder;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;

/**
 * Manages the charge point by listening and sending messages to the charge point.
 */
public class ChargePointManager {
  private final OcppVersion ocppVersion;
  private final OcppMessageParser ocppMessageParser;
  private final OcppMessageBuilder ocppMessageBuilder;
  private final MessageSender messageSender;
  private final UserRepository userRepository;

  /**
   * ChargePointManager's constructor.
   *
   * @param ocppVersion OcppVersion.
   * @param messageSender MessageSender.
   * @param userRepository UserRepository.
   */
  public ChargePointManager(OcppVersion ocppVersion,
                            MessageSender messageSender,
                            UserRepository userRepository) {
    this.ocppVersion = ocppVersion;
    this.ocppMessageParser = OcppMessageParser.instantiateFromVersion(ocppVersion);
    this.ocppMessageBuilder = OcppMessageBuilder.instantiateFromVersion(ocppVersion);
    this.messageSender = messageSender;
    this.userRepository = userRepository;
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
  public String processMessage(WebSocketRequestMessage webSocketRequestMessage) {
    try {
      var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
      var resp = ocppMessageBuilder.buildMessage(webSocketRequestMessage);
      messageSender.sendMessage(new WebSocketResponseMessage(3,
              webSocketRequestMessage.messageId(),
              JsonParser.objectToJsonString(resp)));
      return JsonParser.objectToJsonString(resp);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return "ERROR";
    }
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
