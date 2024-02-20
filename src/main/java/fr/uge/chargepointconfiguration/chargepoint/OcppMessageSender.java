package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

/**
 * An interface to defines a message sender via a web socket.
 */
@FunctionalInterface
public interface OcppMessageSender {
  void sendMessage(OcppMessage ocppMessage, long messageId, int callType, boolean isRequest);
}
