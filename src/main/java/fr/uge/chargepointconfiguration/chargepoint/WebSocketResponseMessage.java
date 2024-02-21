package fr.uge.chargepointconfiguration.chargepoint;

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
  public String toString() {
    return "[" + callType + ",\"" + messageId + "\"," + data + "]";
  }
}
