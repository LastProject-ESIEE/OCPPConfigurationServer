package fr.uge.chargepointconfiguration.chargepoint;

/**
 * A record to define the RequestMessage packet.
 *
 * @param callType    An int.
 * @param messageId   A String.
 * @param messageName A String.
 * @param data        A String.
 */
public record WebSocketRequestMessage(int callType,
                                      String messageId,
                                      String messageName,
                                      String data) {

  /**
   * Parse string message received from the web socket.
   *
   * @param message - received message
   * @return WebSocketMessage
   */
  public static WebSocketRequestMessage parse(String message) {
    var array = message.substring(1, message.length() - 1).split(",", 4);
    int callType = Integer.parseInt(array[0]);
    String messageId = array[1];
    String messageName = array[2].substring(1, array[2].length() - 1);
    return new WebSocketRequestMessage(callType, messageId, messageName, array[3]);
  }

  @Override
  public String toString() {
    return "[" + callType + ",\"" + messageId + "\",\"" + messageName + "\"," + data + "]";
  }
}