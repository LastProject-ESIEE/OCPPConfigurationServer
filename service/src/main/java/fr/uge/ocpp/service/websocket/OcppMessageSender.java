package fr.uge.ocpp.service.websocket;

import fr.uge.ocpp.websocket.ocpp.OcppMessage;

/** Defines a way to send an {@link OcppMessage} through the web socket connection. */
@FunctionalInterface
public interface OcppMessageSender {

  /**
   * Sends an {@link OcppMessage} through the web socket connection.
   *
   * @param ocppMessage the message to send.
   * @param chargePointManager the manager of the charge point the message is sent to.
   */
  void sendMessage(OcppMessage ocppMessage, ChargePointManager chargePointManager);
}
