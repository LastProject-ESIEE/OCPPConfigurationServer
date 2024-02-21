package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketMessage;
import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Optional;

/**
 * Parses an OCPP 2.0.1 message.
 */
public class OcppMessageParser20 implements OcppMessageParser {

  @Override
  public Optional<OcppMessage> parseMessage(WebSocketMessage webSocketMessage) {
    return switch (webSocketMessage.messageName()) {
      case BOOT_NOTIFICATION_REQUEST ->
              Optional.of(JsonParser.stringToObject(BootNotificationRequest20.class,
                      webSocketMessage.data()));
      case STATUS_FIRMWARE_REQUEST ->
              throw new UnsupportedOperationException("TODO : Parse this message");
      case CHANGE_CONFIGURATION_REQUEST ->
              throw new UnsupportedOperationException(
                      "TODO : Parse this messages");
      case OTHER -> Optional.empty(); // Ignoring the message.
    };
  }

  @Override
  public Optional<OcppMessage> parseRequestMessage(WebSocketMessage webSocketMessage) {
    return Optional.empty();
  }

  @Override
  public Optional<OcppMessage> parseResponseMessage(WebSocketMessage requestMessage,
                                                    WebSocketMessage responseMessage) {
    return Optional.empty();
  }

  @Override
  public String transform(OcppMessage message) {
    return JsonParser.objectToJsonString(message);
  }
}
