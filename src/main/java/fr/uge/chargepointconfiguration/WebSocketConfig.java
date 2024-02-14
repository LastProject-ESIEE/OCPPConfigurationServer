package fr.uge.chargepointconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * Defines the web socket configuration.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new MyHandlerGood(), "/myHandler")
            .addInterceptors(new MyHandshakeHandler())
            .setAllowedOrigins("*"); //TODO: maybe check CORS
  }

  /**
   * Returns the web socket current handler.
   *
   * @return WebSocketHandler.
   */
  @Bean
  public WebSocketHandler myHandler() {
    return new MyHandlerGood();
  }


}