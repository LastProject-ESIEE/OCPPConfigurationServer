package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.ChangeConfigurationResponse16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationRequest20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.SetVariablesResponse20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.UpdateFirmwareResponse20;
import java.util.Optional;

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
                                      WebSocketMessageName messageName,
                                      String data) {

  /**
   * Defines the message type sent by the remote.
   */
  public enum WebSocketMessageName {
    BOOT_NOTIFICATION_REQUEST("BootNotification"),
    UPDATE_FIRMWARE_RESPONSE("UpdateFirmware"),
    STATUS_FIRMWARE_REQUEST("StatusFirmware"),
    CHANGE_CONFIGURATION_RESPONSE("ConfigurationResponse"),
    OTHER("Other");

    private final String name;

    WebSocketMessageName(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    /**
     * Converts the message name into an enum to make the process
     * message easier.
     *
     * @param messageName The message type received from the remote.
     * @return The correct enum if found, an OTHER by default.
     */
    public static WebSocketMessageName nameToEnum(String messageName) {
      return switch (messageName) {
        case "BootNotification" -> BOOT_NOTIFICATION_REQUEST;
        case "UpdateFirmware" -> UPDATE_FIRMWARE_RESPONSE;
        case "StatusFirmware" -> STATUS_FIRMWARE_REQUEST;
        case "ChangeConfiguration" -> CHANGE_CONFIGURATION_RESPONSE;
        default -> OTHER;
      };
    }

    /**
     * Converts the OCPP message type into the enum.
     *
     * @param ocppMessage The OCPP message received by the server.
     * @return The correct enum if found, an OTHER by default.
     */
    public static WebSocketMessageName ocppMessageToEnum(OcppMessage ocppMessage) {
      return switch (ocppMessage) {
        case BootNotificationRequest16 ignored -> BOOT_NOTIFICATION_REQUEST;
        case BootNotificationRequest20 ignored -> BOOT_NOTIFICATION_REQUEST;
        case UpdateFirmwareResponse20 ignored -> UPDATE_FIRMWARE_RESPONSE;
        case SetVariablesResponse20 ignored -> CHANGE_CONFIGURATION_RESPONSE;
        case ChangeConfigurationResponse16 ignored -> CHANGE_CONFIGURATION_RESPONSE;
        default -> OTHER;
      };
    }
  }

  /**
   * Parse string message received from the web socket.<br>
   * TODO : improve this method because it is not really safe, the split could be wrong !
   *
   * @param message - received message
   * @return WebSocketMessage
   */
  public static Optional<WebSocketRequestMessage> parse(String message) {
    var array = message.substring(1, message.length() - 1).split(",", 4);
    int callType = Integer.parseInt(array[0]);
    try {
      var messageId = Long.parseLong(array[1].replaceAll("\"", ""));
      var messageName = WebSocketMessageName
              .nameToEnum(array[2]
              .substring(1, array[2].length() - 1));
      return Optional.of(new WebSocketRequestMessage(callType, messageId, messageName, array[3]));
    } catch (NumberFormatException n) {
      return Optional.empty();
    }
  }

  @Override
  public String toString() {
    return "[" + callType + ",\"" + messageId + "\",\""
           + messageName.getName() + "\"," + data + "]";
  }
}