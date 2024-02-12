package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OCPPMessage;

@FunctionalInterface
public interface MessageSender {
    void sendMessage(WebSocketResponseMessage webSocketResponseMessage);
}
