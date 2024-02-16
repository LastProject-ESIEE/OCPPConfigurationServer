package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.OcppMessageBuilder16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.OcppMessageBuilder2;
import java.util.Objects;

/**
 * Interface used to define an OCPP message builder.
 */
public interface OcppMessageBuilder {

  /**
   * Build the correct response to the received WebSocketMessage.<br>
   * For example, a BootNotificationRequest should be answered by a BootNotificationResponse.
   *
   * @param webSocketRequestMessage The received message from the remote host.
   * @return The answer to the received WebSocket message.
   */
  OcppMessage buildMessage(WebSocketRequestMessage webSocketRequestMessage);

  /**
   * Transforms the built message into a String to be sent.
   *
   * @param message The created message by the builder to be sent.
   * @return The OCPP message into a readable String.
   */
  String transform(OcppMessage message);

  /**
   * Instantiate the correct builder according to the OCPP version.
   *
   * @param ocppVersion OcppVersion.
   * @return OcppMessageParser.
   */
  static OcppMessageBuilder instantiateFromVersion(OcppVersion ocppVersion) {
    Objects.requireNonNull(ocppVersion);
    return switch (ocppVersion) {
      case V1_6 -> new OcppMessageBuilder16();
      case V2 -> new OcppMessageBuilder2();
    };
  }
}
