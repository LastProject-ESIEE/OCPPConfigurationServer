package fr.uge.chargepointconfiguration.chargepoint;

/**
 * A record to define a message received by the server from the remote.
 *
 * @param callType    The call's type given by the message.
 * @param messageId   The message's id which is useful if we need to answer.
 * @param messageName An enumeration to define the message type.
 * @param data        The data given by the message, it is in Json format.
 */
public record WebSocketRequestMessage(int callType,
                                      long messageId,
                                      WebSocketMessage.MessageTypeRequest messageName,
                                      String data) implements WebSocketMessage {

  @Override
  public String toString() {
    return "[" + callType + ",\"" + messageId + "\",\""
           + messageName.getName() + "\"," + data + "]";
  }
}