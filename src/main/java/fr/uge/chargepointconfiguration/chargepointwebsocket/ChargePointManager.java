package fr.uge.chargepointconfiguration.chargepointwebsocket;

import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageParser;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppObserver;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.status.StatusRepository;
import java.util.Objects;
import java.util.Optional;

/**
 * Manages the charge point by listening and sending messages to the charge point.
 */
public class ChargePointManager {
  private final OcppVersion ocppVersion;
  private final OcppMessageParser ocppMessageParser;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;
  private final OcppObserver ocppObserver;
  private long currentId = 1;
  private WebSocketMessage pendingRequest = null;

  /**
   * ChargePointManager's constructor.
   *
   * @param ocppVersion           The version of the OCPP protocol (1.6 or 2.0.1).
   * @param ocppMessageSender     The websocket connection used to send data.
   * @param chargepointRepository The chargepoint's repository for database queries.
   */
  public ChargePointManager(OcppVersion ocppVersion,
                            OcppMessageSender ocppMessageSender,
                            ChargepointRepository chargepointRepository,
                            FirmwareRepository firmwareRepository,
                            StatusRepository statusRepository) {
    this.ocppVersion = Objects.requireNonNull(ocppVersion);
    this.ocppMessageParser = OcppMessageParser.instantiateFromVersion(ocppVersion);
    this.chargepointRepository = Objects.requireNonNull(chargepointRepository);
    this.firmwareRepository = Objects.requireNonNull(firmwareRepository);
    this.statusRepository = Objects.requireNonNull(statusRepository);
    this.ocppObserver = OcppObserver.instantiateFromVersion(ocppVersion,
            ocppMessageSender,
            chargepointRepository,
            firmwareRepository,
            statusRepository
    );
  }

  /**
   * Returns the current websocket message id.
   *
   * @return A long representing the current websocket
   */
  public long getCurrentId() {
    return currentId;
  }

  /**
   * Sets the current pending request.<br>
   * If the current pending request is set,
   * we now wait for a response from the chargepoint.
   *
   * @param pendingRequest The request sent to the chargepoint.
   */
  public void setPendingRequest(WebSocketMessage pendingRequest) {
    this.pendingRequest = pendingRequest;
  }

  /**
   * Processes the received websocket message according to the OCPP protocol.
   *
   * @param webSocketMessage The websocket message sent to our server.
   */
  public void processMessage(WebSocketMessage webSocketMessage) {
    Objects.requireNonNull(webSocketMessage);
    Optional<OcppMessage> message;
    if (pendingRequest == null) {
      message = ocppMessageParser.parseRequestMessage(webSocketMessage);
    } else {
      message = ocppMessageParser.parseResponseMessage(pendingRequest,
              webSocketMessage);
      pendingRequest = null;
    }
    if (message.isEmpty()) {
      return;
    }
    var ocppMessage = message.orElseThrow();
    switch (OcppMessage.ocppMessageToMessageType(ocppMessage)) {
      case REQUEST -> currentId = webSocketMessage.messageId();
      default -> {
        // Weird message, ignore it.
      }
    }
    ocppObserver.onMessage(message.orElseThrow(), this);
  }

  /**
   * Does something when the sender has been disconnected.
   */
  public void onDisconnection() {
    // TODO change borne status
  }

  /**
   * Does something if there is an error.
   */
  public void onError() {
    // TODO change borne status
  }
}
