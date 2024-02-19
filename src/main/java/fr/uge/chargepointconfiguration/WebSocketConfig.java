package fr.uge.chargepointconfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Defines the web socket configuration.
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  /**
   * ok.
   *
   * @param registry registry.
   */
  //@RequestMapping("/myHandler")
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new WebSocketHandler(), "/websocket/chargepoint")
            .addInterceptors(new WebSocketHandshakeHandler())
            .setAllowedOrigins("*"); //TODO: maybe check CORS
  }

  /**
   * Returns the web socket current handler.
   *
   * @return WebSocketHandler.
   */
  @Bean
  public org.springframework.web.socket.WebSocketHandler myHandler() {
    return new WebSocketHandler();
  }


}

///**
// * sdf5.
// *
// */
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//  @Override
//  public void configureMessageBroker(MessageBrokerRegistry config) {
//    config.enableSimpleBroker("/topic", "/user");
//    config.setApplicationDestinationPrefixes("/app");
//    config.setUserDestinationPrefix("/user");
//  }
//
//  @Override
//  public void registerStompEndpoints(StompEndpointRegistry registry) {
//    registry.addEndpoint("/myHandler");
//  }
//}