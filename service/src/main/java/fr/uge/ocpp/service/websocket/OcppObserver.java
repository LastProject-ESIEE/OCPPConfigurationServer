package fr.uge.ocpp.service.websocket;

import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.service.websocket.ocpp16.OcppConfigurationObserver16;
import fr.uge.ocpp.service.websocket.ocpp2.OcppConfigurationObserver2;
import fr.uge.ocpp.websocket.ocpp.OcppMessage;
import fr.uge.ocpp.websocket.ocpp.OcppVersion;
import java.util.Objects;
import java.util.Optional;

/**
 * Interface defining the message observer.<br>
 * It listens and processes the messages sent by the charge point.
 */
public interface OcppObserver {

  /**
   * Instantiates the correct observer according to the {@link OcppVersion}.
   *
   * @param ocppVersion the OCPP version negotiated during the handshake.
   * @param chargePointManager the charge point's manager.
   * @param ocppMessageSender the web socket sender.
   * @param chargePointRepository the charge point repository.
   * @param firmwareRepository the firmware repository.
   * @return the {@link OcppObserver} matching the OCPP version.
   */
  static OcppObserver instantiateFromVersion(
      OcppVersion ocppVersion,
      ChargePointManager chargePointManager,
      OcppMessageSender ocppMessageSender,
      ChargePointRepository chargePointRepository,
      FirmwareRepository firmwareRepository) {
    Objects.requireNonNull(ocppVersion);
    return switch (ocppVersion) {
      case V1_6 ->
          new OcppConfigurationObserver16(
              ocppMessageSender, chargePointManager, chargePointRepository, firmwareRepository);
      case V2 ->
          new OcppConfigurationObserver2(
              ocppMessageSender, chargePointManager, chargePointRepository, firmwareRepository);
    };
  }

  /**
   * Processes a received message.
   *
   * @param ocppMessage the {@link OcppMessage} received, null if the message could not be parsed.
   * @return an optional of the message sent in response.
   */
  Optional<OcppMessage> onMessage(OcppMessage ocppMessage);
}
