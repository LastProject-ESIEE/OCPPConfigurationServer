package fr.uge.chargepointconfiguration;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Defines a custom Handler to handle messages received from the web session.
 */
public class MyHandler extends TextWebSocketHandler {

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {

  }
}