package fr.uge.chargepointconfiguration;

import java.io.IOException;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Defines a custom Handler to handle messages received from the web session.
 */
public class WebSocketHandler extends TextWebSocketHandler {

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    System.out.println("handleTextMessage");
  }

  @Override
  public void handleMessage(WebSocketSession session,
                            WebSocketMessage<?> message) throws IOException {
    System.out.println("handleMessage");
    // Handle incoming messages here
    String receivedMessage = (String) message.getPayload();
    // Process the message and send a response if needed
    session.sendMessage(new TextMessage("Received: " + receivedMessage));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    // Perform actions when a new WebSocket connection is established
    System.out.println("afterConnectionEstablished");
  }

  protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    System.out.println("handleBinaryMessage");
  }

  protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    System.out.println("handlePongMessage");
  }

  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    System.out.println("handleTransportError");
  }

  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    System.out.println("afterConnectionClosed");
  }

  /*
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    // Perform actions when a WebSocket connection is closed
  }
   */
}