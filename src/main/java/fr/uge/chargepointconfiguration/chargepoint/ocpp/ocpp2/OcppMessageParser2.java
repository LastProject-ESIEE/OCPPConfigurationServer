package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.tools.JsonParser;

/**
 * Parses an OCPP 2.0.1 message.
 */
public class OcppMessageParser2 implements OcppMessageParser {

  @Override
  public OcppMessage parseMessage(WebSocketRequestMessage webSocketRequestMessage) {
    return switch (webSocketRequestMessage.messageName()) {
      case "BootNotification" ->
              JsonParser.stringToObject(BootNotificationRequest.class,
                      webSocketRequestMessage.data());
      default -> throw new IllegalArgumentException("Message not recognized: "
              + webSocketRequestMessage);
    };
  }

  @Override
  public String transform(OcppMessage message) {
    return JsonParser.objectToJsonString(message);
  }
}
