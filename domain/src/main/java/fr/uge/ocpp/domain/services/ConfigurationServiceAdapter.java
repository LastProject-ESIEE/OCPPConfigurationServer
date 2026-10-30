package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.annotations.DomainService;
import fr.uge.ocpp.domain.exceptions.ConfigurationNotFoundException;
import fr.uge.ocpp.domain.exceptions.FirmwareNotFoundException;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.ports.api.ConfigurationService;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

/** Adapter implementing the configuration use cases on top of the SPI ports. */
@DomainService
public record ConfigurationServiceAdapter(
    ConfigurationRepository configurationRepository,
    FirmwareRepository firmwareRepository,
    Clock clock)
    implements ConfigurationService {

  /**
   * Creates the adapter.
   *
   * @param configurationRepository the configuration persistence port.
   * @param firmwareRepository the firmware persistence port.
   * @param clock the clock used to timestamp updates.
   */
  public ConfigurationServiceAdapter {
    Objects.requireNonNull(configurationRepository);
    Objects.requireNonNull(firmwareRepository);
    Objects.requireNonNull(clock);
  }

  /**
   * Creates a configuration. The firmware referenced by the configuration must exist.
   *
   * @param configuration the configuration to create.
   * @return the created configuration.
   */
  @Override
  public Configuration save(Configuration configuration) {
    Objects.requireNonNull(configuration);
    var firmware = configuration.firmware();
    if (firmware != null) {
      firmwareRepository
          .findById(firmware.id())
          .orElseThrow(
              () -> new FirmwareNotFoundException("Pas de firmware avec l'id : " + firmware.id()));
    }
    var toSave =
        new Configuration(
            null,
            configuration.name(),
            configuration.description(),
            null,
            configuration.configuration(),
            firmware);
    return configurationRepository.save(toSave);
  }

  /**
   * Updates the configuration with the given identifier.
   *
   * @param id the identifier of the configuration to update.
   * @param configuration the new values of the configuration.
   * @return the updated configuration.
   */
  @Override
  public Configuration update(long id, Configuration configuration) {
    Objects.requireNonNull(configuration);
    var oldConfiguration =
        configurationRepository
            .findById(id)
            .orElseThrow(
                () -> new ConfigurationNotFoundException("Pas de configuration avec l'id : " + id));
    var toSave =
        oldConfiguration
            .withName(configuration.name())
            .withDescription(configuration.description())
            .withConfiguration(configuration.configuration())
            .withLastEdit(LocalDateTime.now(clock));
    return configurationRepository.save(toSave);
  }

  /**
   * Finds all the configurations.
   *
   * @return all the configurations.
   */
  @Override
  public Configurations findAll() {
    return configurationRepository.findAllByOrderByIdDesc();
  }

  /**
   * Finds the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration.
   */
  @Override
  public Configuration findById(long id) {
    return configurationRepository
        .findById(id)
        .orElseThrow(
            () -> new ConfigurationNotFoundException("Pas de configuration avec l'id : " + id));
  }

  /**
   * Counts the configurations matching the given request.
   *
   * @param request the search request.
   * @return the number of matching configurations.
   */
  @Override
  public long countTotalWithFilter(String request) {
    return 0;
  }

  /**
   * Finds the configurations matching the given request, paginated.
   *
   * @param request the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding configurations.
   */
  @Override
  public Configurations search(String request, PageQuery pageQuery) {
    // TODO: add query and request
    return Configurations.empty();
  }
}
