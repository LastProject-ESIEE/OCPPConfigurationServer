package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.BootNotificationResponse16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.ChangeConfigurationRequest16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.ChangeConfigurationResponse16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationRequest20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.BootNotificationResponse20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.SetVariablesRequest20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.SetVariablesResponse20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.UpdateFirmwareRequest20;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.UpdateFirmwareResponse20;
import java.util.Objects;

/**
 * Interface used to define an OCPP message.
 */
public interface OcppMessage {

  /**
   * Returns the message type from the given ocpp message.<br>
   * The ocpp message should be a packet sent by the server.
   * It means that if you give a packet received by the server,
   * the returned result would be a MessageType.UNKNOWN.<br>
   * This method returns the type of the current message,
   * so the server would send the correct type.
   *
   * @param ocppMessage The ocpp message sent by the server.
   * @return If the next message should be a request or a response.
   */
  static MessageType ocppMessageToMessageType(OcppMessage ocppMessage) {
    Objects.requireNonNull(ocppMessage);
    return switch (ocppMessage) {
      case BootNotificationResponse20 ignored -> MessageType.RESPONSE;
      case BootNotificationResponse16 ignored -> MessageType.RESPONSE;
      case BootNotificationRequest20 ignored -> MessageType.REQUEST;
      case BootNotificationRequest16 ignored -> MessageType.REQUEST;
      case UpdateFirmwareRequest20 ignored -> MessageType.REQUEST;
      case UpdateFirmwareResponse20 ignored -> MessageType.RESPONSE;
      case SetVariablesRequest20 ignored -> MessageType.REQUEST;
      case ChangeConfigurationRequest16 ignored -> MessageType.REQUEST;
      case SetVariablesResponse20 ignored -> MessageType.RESPONSE;
      case ChangeConfigurationResponse16 ignored -> MessageType.RESPONSE;
      default -> MessageType.UNKNOWN;
    };
  }


}
