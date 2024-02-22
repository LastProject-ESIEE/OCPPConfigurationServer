package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.MessageType;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.ChangeConfigurationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationRequest20;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the message sent or received via a web socket.
 */
public interface WebSocketMessage {

  String data();

  default MessageTypeRequest messageName() {
    return null;
  }

  long messageId();

  /**
   * Defines the message type which the server could receive from the chargepoint.
   */
  enum MessageTypeRequest {
    BOOT_NOTIFICATION_REQUEST("BootNotification"),
    STATUS_FIRMWARE_REQUEST("StatusFirmware"),
    CHANGE_CONFIGURATION_REQUEST("ChangeConfiguration"),
    OTHER("Other");

    private final String name;

    /**
     * MessageTypeReceived's constructor.
     *
     * @param name The correct packet name.
     */
    MessageTypeRequest(String name) {
      this.name = Objects.requireNonNull(name);
    }

    /**
     * Returns the corresponding packet's name.
     *
     * @return The packet's name.
     */
    public String getName() {
      return name;
    }

    /**
     * Converts the OCPP message type into the enum.
     *
     * @param ocppMessage The OCPP message received by the server.
     * @return The correct enum if found, an OTHER by default.
     */
    public static MessageTypeRequest ocppMessageToEnum(OcppMessage ocppMessage) {
      return switch (ocppMessage) {
        case BootNotificationRequest16 ignored -> BOOT_NOTIFICATION_REQUEST;
        case BootNotificationRequest20 ignored -> BOOT_NOTIFICATION_REQUEST;
        case ChangeConfigurationRequest16 ignored -> CHANGE_CONFIGURATION_REQUEST;
        default -> OTHER;
      };
    }

    /**
     * Converts the message name into an enum to make the process
     * message easier.
     *
     * @param messageName The message type received from the remote.
     * @return The correct enum if found, an OTHER by default.
     */
    public static MessageTypeRequest nameToEnum(String messageName) {
      return switch (messageName) {
        case "BootNotification" -> BOOT_NOTIFICATION_REQUEST;
        case "StatusFirmware" -> STATUS_FIRMWARE_REQUEST;
        case "ChangeConfiguration" -> CHANGE_CONFIGURATION_REQUEST;
        default -> OTHER;
      };
    }
  }

  /**
   * Parse string message received from the web socket.
   *
   * @param message - received message
   * @return WebSocketMessage
   */
  static Optional<WebSocketMessage> parse(String message) {
    var array = message.substring(1, message.length() - 1).split(",", 4);
    var callType = Integer.parseInt(array[0]);
    return switch (MessageType.codeToEnum(callType)) {
      case RESPONSE -> {
        try {
          var messageId = Long.parseLong(array[1].replaceAll("\"", ""));
          yield Optional.of(new WebSocketResponseMessage(callType, messageId, array[2]));
        } catch (NumberFormatException n) {
          yield Optional.empty();
        }
      }
      case REQUEST -> {
        try {
          var messageId = Long.parseLong(array[1].replaceAll("\"", ""));
          var messageName = MessageTypeRequest
                  .nameToEnum(array[2]
                          .substring(1, array[2].length() - 1));
          yield Optional.of(
                  new WebSocketRequestMessage(callType, messageId, messageName, array[3])
          );
        } catch (NumberFormatException n) {
          yield Optional.empty();
        }
      }
      case UNKNOWN -> // TODO : Log, unknown type.
              Optional.empty();
    };
  }
}
