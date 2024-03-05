package fr.uge.chargepointconfiguration;

import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ConfigurationServer;
import fr.uge.chargepointconfiguration.configuration.ConfigurationRepository;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.logs.CustomLogger;
import fr.uge.chargepointconfiguration.user.UserRepository;
import java.net.InetSocketAddress;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point to the application.<br>
 * It implements CommandLineRunner.
 */
@SpringBootApplication
public class ChargepointconfigurationApplication implements CommandLineRunner {

  private final UserRepository userRepository;
  private final ChargepointRepository chargepointRepository;
  private final FirmwareRepository firmwareRepository;
  private final ConfigurationRepository configurationRepository;
  private final CustomLogger logger;


  /**
   * The class's constructor.<br>
   * It requires the UserRepository, but it is auto-wired thanks to Spring Boot.
   *
   * @param userRepository UserRepository.
   */
  @Autowired
  public ChargepointconfigurationApplication(UserRepository userRepository,
                                             ChargepointRepository chargepointRepository,
                                             FirmwareRepository firmwareRepository,
                                             ConfigurationRepository configurationRepository,
                                             CustomLogger customLogger) {
    this.userRepository = userRepository;
    this.chargepointRepository = chargepointRepository;
    this.firmwareRepository = Objects.requireNonNull(firmwareRepository);
    this.configurationRepository = configurationRepository;
    this.logger = customLogger;
  }


  /**
   * Launches the server by instantiating the application and running it.
   *
   * @param args String[].
   */
  public static void main(String[] args) {
    SpringApplication.run(ChargepointconfigurationApplication.class, args);
  }

  /**
   * Call on spring server start.
   *
   * @param args program arguments
   * @throws Exception invalid parameter
   */
  @Override
  public void run(String... args) throws Exception {
    var websocketUrl = System.getenv("WEBSOCKET_URL");
    var websocketPortString = System.getenv("WEBSOCKET_PORT");
    if (websocketUrl == null || websocketPortString == null) {
      throw new IllegalStateException("Missing websocket configuration url and/or port.");
    }
    try {
      var websocketPort = Integer.parseInt(websocketPortString);
      Thread.ofPlatform().start(() -> {
        var server = new ConfigurationServer(
                new InetSocketAddress(websocketUrl, websocketPort),
                chargepointRepository,
                firmwareRepository,
                logger
        );
        server.setReuseAddr(true);
        server.run();
      });
    } catch (NumberFormatException e) {
      throw new IllegalStateException("Websocket port invalid value: " + websocketPortString);
    }
  }
}
