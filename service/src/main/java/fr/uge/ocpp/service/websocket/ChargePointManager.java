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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Manages a charge point by listening to and sending messages to it over the web socket. */
public class ChargePointManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChargePointManager.class);

  private final OcppMessageParser ocppMessageParser;
  private final ChargePointRepository chargePointRepository;
  private final OcppObserver ocppObserver;
  private long currentId = 1;
  private WebSocketMessage pendingRequest = null;
  private ChargePoint currentChargepoint = null;

  /**
   * ChargePointManager's constructor.
   *
   * @param ocppVersion the version of the OCPP protocol (1.6 or 2.0.1).
   * @param ocppMessageSender the web socket connection used to send data.
   * @param chargePointRepository the charge point's repository for database queries.
   * @param firmwareRepository the firmware's repository for database queries.
   */
  public ChargePointManager(
      OcppVersion ocppVersion,
      OcppMessageSender ocppMessageSender,
      ChargePointRepository chargePointRepository,
      FirmwareRepository firmwareRepository) {
    this.ocppMessageParser = OcppMessageParser.instantiateFromVersion(ocppVersion);
    this.chargePointRepository = Objects.requireNonNull(chargePointRepository);
    this.ocppObserver =
        OcppObserver.instantiateFromVersion(
            Objects.requireNonNull(ocppVersion),
            this,
            Objects.requireNonNull(ocppMessageSender),
            chargePointRepository,
            Objects.requireNonNull(firmwareRepository));
  }

  /**
   * Returns the current web socket message id.
   *
   * @return a long representing the current web socket message id.
   */
  public long getCurrentId() {
    return currentId;
  }

  /**
   * Sets the current pending request.<br>
   * If the current pending request is set, we now wait for a response from the charge point.
   *
   * @param pendingRequest the {@link WebSocketMessage} request sent to the charge point.
   */
  public void setPendingRequest(WebSocketMessage pendingRequest) {
    this.pendingRequest = pendingRequest;
  }

  /**
   * Returns the charge point currently handled by this manager.
   *
   * @return the current {@link ChargePoint}, null if unknown.
   */
  public ChargePoint getCurrentChargepoint() {
    return currentChargepoint;
  }

  /**
   * Sets the charge point currently handled by this manager.
   *
   * @param currentChargepoint the current {@link ChargePoint}.
   */
  public void setCurrentChargepoint(ChargePoint currentChargepoint) {
    this.currentChargepoint = currentChargepoint;
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
      LOGGER.info("charge point {} disconnected", currentChargepoint.serialNumber());
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
                  .withStatus(currentChargepoint.step(), StatusProcess.FAILED));
      notifyStatusUpdate();
      LOGGER.error("an error occurred on charge point {}", currentChargepoint.serialNumber(), ex);
    }
  }

  /** Logs the current status of the current {@link ChargePoint}. */
  public void notifyStatusUpdate() {
    if (currentChargepoint != null) {
      LOGGER.info(
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
      LOGGER.info(
          "charge point {} process {}: {}",
          currentChargepoint.serialNumber(),
          currentChargepoint.step(),
          currentChargepoint.statusProcess());
    }
  }

  /** Logs the connection of the current {@link ChargePoint}. */
  public void notifyOnConnection() {
    if (currentChargepoint != null) {
      LOGGER.info("charge point {} connected", currentChargepoint.serialNumber());
    }
  }
}
