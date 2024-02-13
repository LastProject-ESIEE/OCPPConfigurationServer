package fr.uge.chargepointconfiguration;

import fr.uge.chargepointconfiguration.chargepoint.ConfigurationServer;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.InetSocketAddress;

/**
 * The main entry point to the application.<br>
 * It implements CommandLineRunner.
 */
@SpringBootApplication
public class ChargepointconfigurationApplication implements CommandLineRunner {

	private final UserRepository userRepository;

	/**
	 * The class's constructor.<br>
	 * It requires the UserRepository, but it is auto-wired thanks to Spring Boot.
	 *
	 * @param userRepository UserRepository.
	 */
	@Autowired
	public ChargepointconfigurationApplication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Launches the server by instantiating the application and running it.
	 *
	 * @param args String[].
	 */
	public static void main(String[] args) {
		SpringApplication.run(ChargepointconfigurationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Thread.ofPlatform().start(() -> {
			var host = "192.168.0.5";
			var port = 8887;
			WebSocketServer server = new ConfigurationServer(new InetSocketAddress(host, port),
							userRepository);
			server.run();
		});
	}
}
