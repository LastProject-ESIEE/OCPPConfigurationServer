package fr.uge.chargepointconfiguration;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Define the handler which manage clients websocket connection.
 */
public class WebSocketHandler extends TextWebSocketHandler {
  // TODO: Refactor concurrency with maybe a ConcurrentArrayList or something like that
  private static final Object lock = new Object();
  private static final ArrayList<WebSocketSession> usersSession = new ArrayList<>();

  // TODO: handleTextMessage and handleMessage maybe removed as we don't need to received messages
  /**
   * Handle TextMessage sent by a client.
   *
   * @param session client websocket session
   * @param message received TextMessage
   */
  @Override
  public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
    System.out.println("handleTextMessage");
  }

  /**
   * Handle message sent by a client.
   *
   * @param session client websocket session
   * @param message message received
   * @throws IOException throw on sendMessage error (ex: connection closed)
   */
  @Override
  public void handleMessage(WebSocketSession session,
                            WebSocketMessage<?> message) throws IOException {
    System.out.println("handleMessage sent by client: " + session.getRemoteAddress());
    String receivedMessage = (String) message.getPayload();
    session.sendMessage(new TextMessage("Received: " + receivedMessage));
  }

  /**
   * Call after a client websocket connection.
   *
   * @param session client websocket session
   */
  @Override
  public void afterConnectionEstablished(@NonNull WebSocketSession session) {
    // Perform actions when a new WebSocket connection is established
    System.out.println("afterConnectionEstablished");
    synchronized (lock) {
      usersSession.add(session);
    }
  }

  /**
   * Call when an error append or if the connection is closed on the client websocket connection.
   *
   * @param session   client websocket session
   * @param exception TODO
   * @throws Exception TODO
   */
  public void handleTransportError(@NonNull WebSocketSession session,
                                   @NonNull Throwable exception) throws Exception {
    System.out.println("handleTransportError");
  }

  /**
   * Call when a session closed the websocket connection.
   *
   * @param session websocket user session
   * @param status  connection status
   * @throws Exception throw
   */
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    System.out.println("afterConnectionClosed with reason: " + status);
    synchronized (lock) {
      usersSession.remove(session);
    }
  }

  /**
   * Send a text message to all clients connected to the websocket server.
   *
   * @param message Message content to be sent
   */
  public static void sendMessageToUsers(String message) {
    synchronized (lock) {
      usersSession.forEach(webSocketSession -> {
        try {
          webSocketSession.sendMessage(new TextMessage(message));
        } catch (IOException e) {
          System.out.println("Failed to sent a message to the client: " + message);
          throw new RuntimeException(e);
        }
      });
    }
  }

}