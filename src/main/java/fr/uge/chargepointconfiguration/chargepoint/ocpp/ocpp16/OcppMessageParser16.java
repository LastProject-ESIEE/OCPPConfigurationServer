package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.tools.JsonParser;

/**
 * Parses an OCPP 1.6 message.
 */
public class OcppMessageParser16 implements OcppMessageParser {

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
