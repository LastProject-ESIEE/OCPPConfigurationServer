package fr.uge.ocpp.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Entry point of the OCPP configuration server API. */
@SpringBootApplication(scanBasePackages = "fr.uge.ocpp")
public class OcppConfigurationServerApplication {

  /**
   * Starts the application.
   *
   * @param args the program arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(OcppConfigurationServerApplication.class, args);
  }
}
