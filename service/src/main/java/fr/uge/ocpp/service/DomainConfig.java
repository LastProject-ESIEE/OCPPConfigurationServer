package fr.uge.ocpp.service;

import fr.uge.ocpp.domain.ports.api.ChargePointService;
import fr.uge.ocpp.domain.ports.api.ConfigurationService;
import fr.uge.ocpp.domain.ports.api.FirmwareService;
import fr.uge.ocpp.domain.ports.api.TypeAllowedService;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepository;
import fr.uge.ocpp.domain.services.ChargePointServiceAdapter;
import fr.uge.ocpp.domain.services.ConfigurationServiceAdapter;
import fr.uge.ocpp.domain.services.FirmwareServiceAdapter;
import fr.uge.ocpp.domain.services.TypeAllowedServiceAdapter;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires the domain services with their SPI adapters provided by the database module. */
@Configuration
public class DomainConfig {

  /**
   * Provides the clock used by the domain to timestamp updates.
   *
   * @return the system clock.
   */
  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  /**
   * Provides the charge point use cases.
   *
   * @param chargePointRepository    the charge point persistence port.
   * @param configurationRepository the configuration persistence port.
   * @param clock                    the clock used to timestamp updates.
   * @return the charge point service.
   */
  @Bean
  public ChargePointService chargePointService(ChargePointRepository chargePointRepository,
      ConfigurationRepository configurationRepository, Clock clock) {
    return new ChargePointServiceAdapter(chargePointRepository, configurationRepository, clock);
  }

  /**
   * Provides the configuration use cases.
   *
   * @param configurationRepository the configuration persistence port.
   * @param firmwareRepository       the firmware persistence port.
   * @param clock                    the clock used to timestamp updates.
   * @return the configuration service.
   */
  @Bean
  public ConfigurationService configurationService(
      ConfigurationRepository configurationRepository,
      FirmwareRepository firmwareRepository, Clock clock) {
    return new ConfigurationServiceAdapter(configurationRepository, firmwareRepository, clock);
  }

  /**
   * Provides the firmware use cases.
   *
   * @param firmwareRepository      the firmware persistence port.
   * @param typeAllowedRepository the allowed type persistence port.
   * @return the firmware service.
   */
  @Bean
  public FirmwareService firmwareService(FirmwareRepository firmwareRepository,
      TypeAllowedRepository typeAllowedRepository) {
    return new FirmwareServiceAdapter(firmwareRepository, typeAllowedRepository);
  }

  /**
   * Provides the allowed type use cases.
   *
   * @param typeAllowedRepository the allowed type persistence port.
   * @return the allowed type service.
   */
  @Bean
  public TypeAllowedService typeAllowedService(TypeAllowedRepository typeAllowedRepository) {
    return new TypeAllowedServiceAdapter(typeAllowedRepository);
  }
}
