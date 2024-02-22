package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepointwebsocket.WebSocketMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import java.util.Optional;

/**
 * Parses an OCPP 2.0.1 message.
 */
public class OcppMessageParser20 implements OcppMessageParser {

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