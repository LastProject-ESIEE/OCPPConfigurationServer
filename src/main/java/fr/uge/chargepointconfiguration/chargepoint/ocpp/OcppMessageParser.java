package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import fr.uge.chargepointconfiguration.chargepoint.WebSocketMessage;
import fr.uge.chargepointconfiguration.chargepoint.WebSocketRequestMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16.OcppMessageParser16;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.OcppMessageParser20;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface used to define an OCPP message parser.<br>
 * TODO : We could use an Observer to avoid redundancy.
 */
public interface OcppMessageParser {

  /**
   * Parses the request web socket message sent by the chargepoint
   * into an OCPP message.<br>
   * Returns an empty optional if the packet is unknown.
   *
   * @param webSocketMessage The received message.
   * @return An optional of an OCPP message.
   */
  Optional<OcppMessage> parseRequestMessage(WebSocketMessage webSocketMessage);

  /**
   * Parses the request web socket message sent by the server
   * into an OCPP message.<br>
   * This method should be called after the server sent a request to the chargepoint.<br>
   * Returns an empty optional if the packet is unknown.
   *
   * @param requestMessage The message the server sent to the chargepoint.
   * @param responseMessage The message sent by the chargepoint.
   * @return An optional of an OCPP message.
   */
  Optional<OcppMessage> parseResponseMessage(WebSocketMessage requestMessage,
                                             WebSocketMessage responseMessage);

  String transform(OcppMessage message);

  /**
   * Instantiate the correct parser according to the OCPP version.
   *
   * @param ocppVersion OcppVersion.
   * @return OcppMessageParser.
   */
  static OcppMessageParser instantiateFromVersion(OcppVersion ocppVersion) {
    Objects.requireNonNull(ocppVersion);
    return switch (ocppVersion) {
      case V1_6 -> new OcppMessageParser16();
      case V2 -> new OcppMessageParser20();
    };
  }
}
