package fr.uge.chargepointconfiguration;

import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * ok.
 */
public class WebSocketHandshakeHandler implements HandshakeInterceptor {
  @Override
  public boolean beforeHandshake(ServerHttpRequest request,
                                 ServerHttpResponse response,
                                 WebSocketHandler wsHandler, Map<String,
          Object> attributes) throws Exception {
    System.out.println("beforeHandshake");
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                             WebSocketHandler wsHandler, Exception exception) {
    System.out.println("afterHandshake");
  }
}