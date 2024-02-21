package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketMessage;
import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Optional;

/**
 * Parses an OCPP 1.6 message.
 */
public class OcppMessageParser16 implements OcppMessageParser {

  @Override
  public Optional<OcppMessage> parseMessage(WebSocketMessage webSocketMessage) {
    var type = webSocketMessage.messageName();
    if (type == null) {
      return Optional.empty();
    }
    return switch (webSocketMessage.messageName()) {
      case BOOT_NOTIFICATION_REQUEST -> {
        System.out.println(webSocketMessage.data());
        yield Optional.of(JsonParser.stringToObject(BootNotificationRequest16.class,
                webSocketMessage.data()));
      }
      case STATUS_FIRMWARE_REQUEST ->
              throw new UnsupportedOperationException("TODO : Parse this message");
      case CHANGE_CONFIGURATION_REQUEST ->
              throw new UnsupportedOperationException(
                      "TODO : Parse this message");
      case OTHER -> Optional.empty(); // Ignoring the message.
    };
  }

  @Override
  public String transform(OcppMessage message) {
    return JsonParser.objectToJsonString(message);
  }
}
