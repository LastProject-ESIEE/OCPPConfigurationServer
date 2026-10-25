package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.annotations.DomainService;
import fr.uge.ocpp.domain.exceptions.ConfigurationNotFoundException;
import fr.uge.ocpp.domain.exceptions.FirmwareNotFoundException;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.ports.api.ConfigurationService;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@DomainService
public record ConfigurationServiceAdapter(ConfigurationRepository configurationRepository,
                                          FirmwareRepository firmwareRepository,
                                          Clock clock) implements ConfigurationService {

  public ConfigurationServiceAdapter {
    Objects.requireNonNull(configurationRepository);
    Objects.requireNonNull(firmwareRepository);
    Objects.requireNonNull(clock);
  }

  @Override
  public Configuration save(Configuration configuration) {
    Firmware firmware = firmwareRepository.findById(configuration.firmware().id()).orElseThrow(() -> new FirmwareNotFoundException("Firmware not found"));
    Configuration toSave = new Configuration(null, configuration.name(), configuration.description(), null, configuration.configuration(), firmware);

    return configurationRepository.save(toSave);
  }

  @Override
  public Configuration update(long id, Configuration configuration) {
    Configuration oldConfiguration = configurationRepository.findById(id).orElseThrow(() -> new ConfigurationNotFoundException("Configuration not found"));
    Configuration toSave = oldConfiguration.withName(configuration.name())
      .withDescription(configuration.description())
      .withConfiguration(configuration.configuration())
      .withLastEdit(LocalDateTime.now(clock));

    return configurationRepository.save(toSave);
  }

  @Override
  public Configurations findAll() {
    return null;
  }

  @Override
  public Configuration findById(long id) {
    return null;
  }

  @Override
  public long countTotalWithFilter(String request) {
    return 0;
  }

  @Override
  public Configurations search(String request, PageQuery pageQuery) {
    return null;
  }

}
