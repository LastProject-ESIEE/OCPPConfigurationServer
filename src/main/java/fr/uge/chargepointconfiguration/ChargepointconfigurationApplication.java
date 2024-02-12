package fr.uge.chargepointconfiguration;

import fr.uge.chargepointconfiguration.chargepoint.ConfigurationServer;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;

@SpringBootApplication
public class ChargepointconfigurationApplication /*implements CommandLineRunner*/ {

	private final UserRepository userRepository;

	@Autowired
	public ChargepointconfigurationApplication(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ChargepointconfigurationApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		Thread.ofPlatform().start(() -> {
//			var host = "192.168.0.5";
//			//var host = "localhost";
//			var port = 8887;
//
//			WebSocketServer server = new ConfigurationServer(new InetSocketAddress(host, port), userRepository);
//			server.run();
//		});
//
//
//
////		Users user1 = new Users("John", "john@domain.com");
////		Users user2 = new Users("Julie", "julie@domain.com");
////		userRepository.save(user1);
////		userRepository.save(user2);
////		userRepository.findAll().forEach(System.out::println);
//	}



}
