package fr.uge.ocpp.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/** Entry point of the OCPP configuration server API. */
@SpringBootApplication(scanBasePackages = "fr.uge.ocpp")
@ConfigurationPropertiesScan
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
