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

  Optional<OcppMessage> parseMessage(WebSocketMessage webSocketMessage);

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
