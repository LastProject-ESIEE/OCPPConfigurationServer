package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Optional;

/**
 * Parses an OCPP 2.0.1 message.
 */
public class OcppMessageParser2 implements OcppMessageParser {

  @Override
  public Optional<OcppMessage> parseMessage(WebSocketRequestMessage webSocketRequestMessage) {
    return switch (webSocketRequestMessage.messageName()) {
      case BOOT_NOTIFICATION_REQUEST ->
              Optional.of(JsonParser.stringToObject(BootNotificationRequest.class,
                      webSocketRequestMessage.data()));
      case UPDATE_FIRMWARE_RESPONSE ->
              throw new UnsupportedOperationException(
                      "TODO : Parse this message");
      case STATUS_FIRMWARE_REQUEST ->
              throw new UnsupportedOperationException("TODO : Parse this message");
      case CHANGE_CONFIGURATION_RESPONSE ->
              throw new UnsupportedOperationException(
                      "TODO : Parse this messages");
      case OTHER -> Optional.empty(); // Ignoring the message.
    };
  }

  @Override
  public String transform(OcppMessage message) {
    return JsonParser.objectToJsonString(message);
  }
}
