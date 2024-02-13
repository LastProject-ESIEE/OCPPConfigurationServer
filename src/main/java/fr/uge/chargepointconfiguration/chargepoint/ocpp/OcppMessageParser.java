package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;

/**
 * Interface used to define an OCPP message parser.
 */
public interface OcppMessageParser {

  OcppMessage parseMessage(WebSocketRequestMessage webSocketRequestMessage);

  String transform(OcppMessage message);
}
