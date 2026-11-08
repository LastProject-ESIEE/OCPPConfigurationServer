package fr.uge.ocpp.service.websocket;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.websocket.WebSocketMessage;
import fr.uge.ocpp.websocket.ocpp.MessageType;
import fr.uge.ocpp.websocket.ocpp.OcppMessage;
import fr.uge.ocpp.websocket.ocpp.OcppMessageParser;
import fr.uge.ocpp.websocket.ocpp.OcppVersion;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/** Manages a charge point by listening to and sending messages to it over the web socket. */
@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class ChargePointManager {

  private final OcppMessageParser ocppMessageParser;
  private final ChargePointRepository chargePointRepository;
  private final OcppObserver ocppObserver;
  private long currentId;
  private WebSocketMessage pendingRequest;
  private ChargePoint currentChargepoint;

  public ChargePointManager(
      OcppVersion ocppVersion,
      OcppMessageSender ocppMessageSender,
      ChargePointRepository chargePointRepository,
      FirmwareRepository firmwareRepository,
      String finalServerAddress) {
    this.ocppMessageParser = OcppMessageParser.instantiateFromVersion(ocppVersion);
    this.chargePointRepository = Objects.requireNonNull(chargePointRepository);
    this.ocppObserver =
        OcppObserver.instantiateFromVersion(
            Objects.requireNonNull(ocppVersion),
            this,
            Objects.requireNonNull(ocppMessageSender),
            chargePointRepository,
            Objects.requireNonNull(firmwareRepository),
            finalServerAddress);
  }

  /**
   * Processes the received web socket message according to the OCPP protocol.
   *
   * @param webSocketMessage the {@link WebSocketMessage} sent to our server.
   * @return an optional of the {@link OcppMessage} sent in response.
   */
  public Optional<OcppMessage> processMessage(WebSocketMessage webSocketMessage) {
    Objects.requireNonNull(webSocketMessage);
    Optional<OcppMessage> message;
    if (webSocketMessage.isRequest()) {
      message = ocppMessageParser.parseRequestMessage(webSocketMessage);
    } else {
      message = ocppMessageParser.parseResponseMessage(pendingRequest, webSocketMessage);
      pendingRequest = null;
    }
    if (message.isEmpty()) {
      return ocppObserver.onMessage(null);
    }
    var ocppMessage = message.orElseThrow();
    if (OcppMessage.ocppMessageToMessageType(ocppMessage) == MessageType.REQUEST) {
      currentId = webSocketMessage.messageId();
    } else {
      currentId += 1;
    }
    return ocppObserver.onMessage(ocppMessage);
  }

  /** Does something when the sender has been disconnected. */
  public void onDisconnection() {
    if (currentChargepoint != null) {
      currentChargepoint = chargePointRepository.save(currentChargepoint.withState(false));
      notifyStatusUpdate();
      log.info("charge point {} disconnected", currentChargepoint.serialNumber());
    }
  }

  /**
   * Does something if there is an error.
   *
   * @param ex the exception raised.
   */
  public void onError(Exception ex) {
    if (currentChargepoint != null) {
      currentChargepoint =
          chargePointRepository.save(
              currentChargepoint
                  .withError(ex.toString())
                  .withStatusProcess(StatusProcess.FAILED)
                  .withStep(currentChargepoint.step()));
      notifyStatusUpdate();
      log.error("an error occurred on charge point {}", currentChargepoint.serialNumber(), ex);
    }
  }

  /** Logs the current status of the current {@link ChargePoint}. */
  public void notifyStatusUpdate() {
    if (currentChargepoint != null) {
      log.info(
          "charge point {} status update: state={} step={} status={}",
          currentChargepoint.serialNumber(),
          currentChargepoint.state(),
          currentChargepoint.step(),
          currentChargepoint.statusProcess());
    }
  }

  /** Logs the end of the process of the current {@link ChargePoint}. */
  public void notifyProcess() {
    if (currentChargepoint != null) {
      log.info(
          "charge point {} process {}: {}",
          currentChargepoint.serialNumber(),
          currentChargepoint.step(),
          currentChargepoint.statusProcess());
    }
  }

  /** Logs the connection of the current {@link ChargePoint}. */
  public void notifyOnConnection() {
    if (currentChargepoint != null) {
      log.info("charge point {} connected", currentChargepoint.serialNumber());
    }
  }
}
