package fr.uge.chargepointconfiguration.chargepointwebsocket;

/**
 * Defines the web socket response message.
 *
 * @param callType  int.
 * @param messageId String.
 * @param data      String.
 */
public record WebSocketResponseMessage(int callType, long messageId, String data)
        implements WebSocketMessage {

  @Override
  public boolean isRequest() {
    return false;
  }

  @Override
  public String toString() {
    return "[" + callType + ",\"" + messageId + "\"," + data + "]";
  }
}
