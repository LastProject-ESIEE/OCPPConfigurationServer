package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;

public interface OCPPMessageParser {
    OCPPMessage parseMessage(WebSocketRequestMessage webSocketRequestMessage);
    String transform(OCPPMessage message);
}
