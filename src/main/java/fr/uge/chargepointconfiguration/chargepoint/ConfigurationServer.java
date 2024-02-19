package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import fr.uge.chargepointconfiguration.repository.StatusRepository;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * Configures the web socket server.
 */
public class ConfigurationServer extends WebSocketServer {
  private static final Logger LOGGER = LogManager.getLogger(ConfigurationServer.class);
  private final HashMap<InetSocketAddress, ChargePointManager> chargePoints = new HashMap<>();
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final StatusRepository statusRepository;

  /**
   * ConfigurationServer's constructor.
   *
   * @param address InetSocketAddress.
   */
  public ConfigurationServer(InetSocketAddress address,
                             ChargepointRepository chargepointRepository,
                             FirmwareRepository firmwareRepository,
                             StatusRepository statusRepository) {
    super(address);
    this.chargepointRepository = chargepointRepository;
    this.firmwareRepository = Objects.requireNonNull(firmwareRepository);
    this.statusRepository = Objects.requireNonNull(statusRepository);
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    //conn.send("Welcome to the server!");
    LOGGER.info("new connection to " + conn.getRemoteSocketAddress());
    var ocppVersion = OcppVersion.parse(handshake.getFieldValue("Sec-Websocket-Protocol"));
    chargePoints.putIfAbsent(conn.getRemoteSocketAddress(),
            new ChargePointManager(ocppVersion.orElseThrow(),
                    message -> conn.send(message.toString()),
                    chargepointRepository,
                    firmwareRepository,
                    statusRepository));
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    LOGGER.warn("closed "
            + conn.getRemoteSocketAddress()
            + " with exit code "
            + code
            + " additional info: "
            + reason);
    chargePoints.remove(conn.getRemoteSocketAddress());
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    var remote = conn.getRemoteSocketAddress();
    LOGGER.info("received message from "
            + remote
            + ": "
            + message);
    var webSocketMessage = WebSocketRequestMessage.parse(message);
    var processed = chargePoints.get(conn.getRemoteSocketAddress())
            .processMessage(webSocketMessage);
    if (processed.isPresent()) {
      LOGGER.info("sent message to "
              + remote
              + ": "
              + processed.orElseThrow());
    } else {
      LOGGER.info("sent message to "
              + remote
              + ": "
              + "IGNORING MESSAGE");
    }
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    LOGGER.info("received ByteBuffer from "
            + conn.getRemoteSocketAddress());
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    LOGGER.error("an error occurred on connection "
            + conn.getRemoteSocketAddress()
            + ":"
            + ex);
  }

  @Override
  public void onStart() {
    LOGGER.info("server started successfully");
  }
}