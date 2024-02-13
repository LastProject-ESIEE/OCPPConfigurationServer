package fr.uge.chargepointconfiguration.chargepoint;

/**
 * An interface to defines a message sender via a web socket.
 */
@FunctionalInterface
public interface MessageSender {
  void sendMessage(WebSocketResponseMessage webSocketResponseMessage);
}
