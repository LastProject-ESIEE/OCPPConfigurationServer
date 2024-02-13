package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;

public interface OCPPMessageParser {
    OcppMessage parseMessage(WebSocketRequestMessage webSocketRequestMessage);
    String transform(OcppMessage message);
}
