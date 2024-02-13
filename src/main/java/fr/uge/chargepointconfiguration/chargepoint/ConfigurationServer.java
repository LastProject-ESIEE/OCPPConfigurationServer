package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppVersion;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * Configures the web socket server.
 */
public class ConfigurationServer extends WebSocketServer {
  private final HashMap<InetSocketAddress, ChargePointManager> chargePoints = new HashMap<>();
  private final UserRepository userRepository;

  /**
   * ConfigurationServer's constructor.
   *
   * @param address InetSocketAddress.
   * @param userRepository UserRepository.
   */
  public ConfigurationServer(InetSocketAddress address, UserRepository userRepository) {
    super(address);
    this.userRepository = userRepository;
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    //conn.send("Welcome to the server!");
    System.out.println("new connection to " + conn.getRemoteSocketAddress());
    var ocppVersion = OcppVersion.parse(handshake.getFieldValue("Sec-Websocket-Protocol"));
    chargePoints.put(conn.getRemoteSocketAddress(),
            new ChargePointManager(ocppVersion.orElseThrow(),
                    message -> conn.send(message.toString()), userRepository));
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("closed "
            + conn.getRemoteSocketAddress()
            + " with exit code "
            + code
            + " additional info: "
            + reason);
    chargePoints.remove(conn.getRemoteSocketAddress());
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println("received message from "
            + conn.getRemoteSocketAddress()
            + ": "
            + message);
    var webSocketMessage = WebSocketRequestMessage.parse(message);
    chargePoints.get(conn.getRemoteSocketAddress()).processMessage(webSocketMessage);
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println("received ByteBuffer from "
            + conn.getRemoteSocketAddress());
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    System.err.println("an error occurred on connection "
            + conn.getRemoteSocketAddress()
            + ":"
            + ex);
  }

  @Override
  public void onStart() {
    System.out.println("server started successfully");
  }
}