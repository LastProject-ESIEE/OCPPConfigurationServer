package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageBuilder;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.ChangeConfigurationRequest;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.ArrayList;

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
   * Processes the message from the sender in OCPP.
   *
   * @param webSocketRequestMessage WebSocketRequestMessage.
   */
  public String processMessage(WebSocketRequestMessage webSocketRequestMessage) {
    try {
      var message = ocppMessageParser.parseMessage(webSocketRequestMessage);
      var resp = ocppMessageBuilder.buildMessage(webSocketRequestMessage);
      messageSender.sendMessage(new WebSocketResponseMessage(3,
              webSocketRequestMessage.messageId(),
              JsonParser.objectToJsonString(resp)));
      messageSender.sendMessage(new WebSocketResponseMessage(3,
              webSocketRequestMessage.messageId(),
              JsonParser.objectToJsonString(new ChangeConfigurationRequest(new ArrayList<>()))));
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
