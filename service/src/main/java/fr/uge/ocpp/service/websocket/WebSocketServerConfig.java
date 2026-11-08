package fr.uge.ocpp.service.websocket;

import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import java.net.InetSocketAddress;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Starts the OCPP web socket server (accepting charge point connections) alongside the application.
 * <br>
 * The bind address is configured with the {@code websocket.url} and {@code websocket.port}
 * properties (see {@link WebsocketProperties}), overridable with the {@code WEBSOCKET_URL} and
 * {@code WEBSOCKET_PORT} environment variables.
 */
@Configuration
public class WebSocketServerConfig {

  /**
   * Starts the {@link ConfigurationServer} on a dedicated thread once the application is ready.
   *
   * @param chargePointRepository the charge point repository.
   * @param firmwareRepository the firmware repository.
   * @param websocketProperties the web socket server configuration.
   * @return the {@link CommandLineRunner} starting the web socket server.
   */
  @Bean
  CommandLineRunner ocppWebSocketServerRunner(
      ChargePointRepository chargePointRepository,
      FirmwareRepository firmwareRepository,
      WebsocketProperties websocketProperties) {
    return args -> {
      var websocketUrl = websocketProperties.url();
      var websocketPortString = websocketProperties.port();
      if (websocketUrl == null
          || websocketUrl.isBlank()
          || websocketPortString == null
          || websocketPortString.isBlank()) {
        throw new IllegalStateException("Missing websocket configuration url and/or port.");
      }
      try {
        var websocketPort = Integer.parseInt(websocketPortString);
        var server =
            new ConfigurationServer(
                new InetSocketAddress(websocketUrl, websocketPort),
                chargePointRepository,
                firmwareRepository,
                websocketProperties.finalServerAddress());
        server.setReuseAddr(true);
        Thread.ofPlatform().start(server::run);
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Websocket port invalid value: " + websocketPortString);
      }
    };
  }
}
