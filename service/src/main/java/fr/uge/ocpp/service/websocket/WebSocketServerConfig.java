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
 * The bind address is configured with the {@code WEBSOCKET_URL} and {@code WEBSOCKET_PORT}
 * environment variables.
 */
@Configuration
public class WebSocketServerConfig {

  /**
   * Starts the {@link ConfigurationServer} on a dedicated thread once the application is ready.
   *
   * @param chargePointRepository the charge point repository.
   * @param firmwareRepository the firmware repository.
   * @return the {@link CommandLineRunner} starting the web socket server.
   */
  @Bean
  CommandLineRunner ocppWebSocketServerRunner(
      ChargePointRepository chargePointRepository, FirmwareRepository firmwareRepository) {
    return args -> {
      var websocketUrl = System.getenv("WEBSOCKET_URL");
      var websocketPortString = System.getenv("WEBSOCKET_PORT");
      if (websocketUrl == null || websocketPortString == null) {
        throw new IllegalStateException("Missing websocket configuration url and/or port.");
      }
      try {
        var websocketPort = Integer.parseInt(websocketPortString);
        var server =
            new ConfigurationServer(
                new InetSocketAddress(websocketUrl, websocketPort),
                chargePointRepository,
                firmwareRepository);
        server.setReuseAddr(true);
        Thread.ofPlatform().start(server::run);
      } catch (NumberFormatException e) {
        throw new IllegalStateException("Websocket port invalid value: " + websocketPortString);
      }
    };
  }
}
