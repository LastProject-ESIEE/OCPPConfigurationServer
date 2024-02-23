package fr.uge.chargepointconfiguration.chargepointwebsocket;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessage;

/**
 * An interface to defines a message sender via a web socket.
 */
@FunctionalInterface
public interface OcppMessageSender {
  void sendMessage(OcppMessage ocppMessage, ChargePointManager chargePointManager);
}
