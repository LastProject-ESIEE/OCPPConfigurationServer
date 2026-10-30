package fr.uge.ocpp.service.websocket;

import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.websocket.WebSocketMessage;
import fr.uge.ocpp.websocket.WebSocketRequestMessage;
import fr.uge.ocpp.websocket.WebSocketResponseMessage;
import fr.uge.ocpp.websocket.ocpp.MessageType;
import fr.uge.ocpp.websocket.ocpp.OcppMessage;
import fr.uge.ocpp.websocket.ocpp.OcppVersion;
import fr.uge.ocpp.websocket.tools.JsonParser;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web socket server accepting the charge points' OCPP connections.<br>
 * The OCPP version of a connection is read from the {@code Sec-Websocket-Protocol} handshake header
 * (see {@link OcppVersion#parse(String)}).
 */
public class ConfigurationServer extends WebSocketServer {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationServer.class);

  private final Map<InetSocketAddress, ChargePointManager> chargePoints = new ConcurrentHashMap<>();
  private final ChargePointRepository chargePointRepository;
  private final FirmwareRepository firmwareRepository;

  /**
   * {@link ConfigurationServer}'s constructor.
   *
   * @param address the remote address to bind to.
   * @param chargePointRepository the charge point repository.
   * @param firmwareRepository the firmware repository.
   */
  public ConfigurationServer(
      InetSocketAddress address,
      ChargePointRepository chargePointRepository,
      FirmwareRepository firmwareRepository) {
    super(Objects.requireNonNull(address));
    this.chargePointRepository = Objects.requireNonNull(chargePointRepository);
    this.firmwareRepository = Objects.requireNonNull(firmwareRepository);
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    Objects.requireNonNull(conn);
    Objects.requireNonNull(handshake);
    LOGGER.info("new connection to {}", conn.getRemoteSocketAddress());
    var ocppVersion = OcppVersion.parse(handshake.getFieldValue("Sec-Websocket-Protocol"));
    if (ocppVersion.isPresent()) {
      chargePoints.putIfAbsent(
          conn.getRemoteSocketAddress(), instantiate(ocppVersion.orElseThrow(), conn));
    } else {
      LOGGER.warn("unknown OCPP version for {}", conn.getRemoteSocketAddress());
    }
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    Objects.requireNonNull(conn);
    LOGGER.warn(
        "closed {} with exit code {} additional info: {}",
        conn.getRemoteSocketAddress(),
        code,
        reason);
    var chargepoint = chargePoints.getOrDefault(conn.getRemoteSocketAddress(), null);
    if (chargepoint != null) {
      chargepoint.onDisconnection();
    }
    chargePoints.remove(conn.getRemoteSocketAddress());
    conn.close();
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    Objects.requireNonNull(conn);
    Objects.requireNonNull(message);
    var remote = conn.getRemoteSocketAddress();
    var webSocketMessage = WebSocketMessage.parse(message);
    if (webSocketMessage.isEmpty()) {
      LOGGER.warn("failed to parse message from {}: {}", remote, message);
      return;
    }
    if (webSocketMessage.orElseThrow().isRequest()) {
      LOGGER.info("received request from {}: {}", remote, message);
    } else {
      LOGGER.info("received response from {}: {}", remote, message);
    }
    chargePoints.get(remote).processMessage(webSocketMessage.orElseThrow());
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    Objects.requireNonNull(conn);
    Objects.requireNonNull(message);
    LOGGER.info("received ByteBuffer from {}", conn.getRemoteSocketAddress());
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    Objects.requireNonNull(ex);
    LOGGER.error(
        "an error occurred on connection {}: {}",
        conn == null ? "" : conn.getRemoteSocketAddress(),
        ex.toString());
    if (conn != null) {
      var chargepoint = chargePoints.get(conn.getRemoteSocketAddress());
      if (chargepoint != null) {
        chargepoint.onError(ex);
      }
    }
  }

  @Override
  public void onStart() {
    LOGGER.info("OCPP web socket server started successfully on {}", getAddress());
  }

  /**
   * Instantiates the {@link ChargePointManager} according to the OCPP version.
   *
   * @param ocppVersion the OCPP version of the connection.
   * @param conn the web socket connection.
   * @return the {@link ChargePointManager}.
   */
  private ChargePointManager instantiate(OcppVersion ocppVersion, WebSocket conn) {
    return new ChargePointManager(
        ocppVersion,
        (ocppMessage, chargePointManager) -> {
          switch (OcppMessage.ocppMessageToMessageType(ocppMessage)) {
            case REQUEST -> {
              var request =
                  new WebSocketRequestMessage(
                      MessageType.REQUEST.getCallType(),
                      chargePointManager.getCurrentId(),
                      WebSocketMessage.MessageTypeRequest.ocppMessageToEnum(ocppMessage),
                      JsonParser.objectToJsonString(ocppMessage));
              chargePointManager.setPendingRequest(request);
              conn.send(request.toString());
              LOGGER.info("sent request to {}: {}", conn.getRemoteSocketAddress(), request);
            }
            case RESPONSE -> {
              var response =
                  new WebSocketResponseMessage(
                      MessageType.RESPONSE.getCallType(),
                      chargePointManager.getCurrentId(),
                      JsonParser.objectToJsonString(ocppMessage));
              conn.send(response.toString());
              LOGGER.info("sent response to {}: {}", conn.getRemoteSocketAddress(), response);
            }
            default -> LOGGER.error("tried to send an unknown packet: {}", ocppMessage);
          }
        },
        chargePointRepository,
        firmwareRepository);
  }
}
