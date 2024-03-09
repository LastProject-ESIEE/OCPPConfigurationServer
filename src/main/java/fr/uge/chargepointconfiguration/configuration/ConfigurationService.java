package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * A ConfigurationService doing database manipulations.
 */
@Service
public class ConfigurationService {

  private final ConfigurationRepository configurationRepository;

  private final FirmwareRepository firmwareRepository;

  /**
   * ConfigurationService's constructor.
   *
   * @param configurationRepository A ConfigurationRepository accessing to database.
   * @param firmwareRepository      A FirmwareRepository accessing to database.
   */
  @Autowired
  public ConfigurationService(ConfigurationRepository configurationRepository,
                              FirmwareRepository firmwareRepository) {
    this.configurationRepository = configurationRepository;
    this.firmwareRepository = firmwareRepository;
  }

  /**
   * Create a configuration.
   *
   * @param createConfigurationDto All the necessary information for a configuration creation.
   * @return A configuration created with its information.
   */
  public ConfigurationDto save(CreateConfigurationDto createConfigurationDto) {
    var configuration = configurationRepository.save(new Configuration(
        createConfigurationDto.name(),
        createConfigurationDto.description(),
        createConfigurationDto.configuration(),
        firmwareRepository.findById(createConfigurationDto.firmware()).orElseThrow()));

    return configuration.toDto(); // TODO refacto for dealing with multiple entity<DTO>
  }

  /**
   * Update a configuration.
   *
   * @param updateConfigurationDto All the necessary information for a configuration update.
   * @return A configuration created with its information.
   */
  public Optional<ConfigurationDto> update(UpdateConfigurationDto updateConfigurationDto) {
    var currentConfiguration = configurationRepository.findById(updateConfigurationDto.id());
    return currentConfiguration.map(configuration -> {
      configuration.setName(updateConfigurationDto.name());
      configuration.setDescription(updateConfigurationDto.description());
      configuration.setConfiguration(updateConfigurationDto.configuration());
      configuration.setLastEdit(new Timestamp(System.currentTimeMillis()));
      configuration.setFirmware(
              firmwareRepository.findById(updateConfigurationDto.firmware()).orElseThrow()
      );
      configurationRepository.save(configuration);
      return configuration.toDto();
    });
  }

  /**
   * Get all the configurations.
   *
   * @return A list of configurations.
   */
  public List<ConfigurationDto> getAllConfigurations() {
    return configurationRepository.findAllByOrderByIdDesc()
          .stream()
        .map(Configuration::toDto)
          .toList();
  }

  /**
   * Get a configuration by id.
   *
   * @return Selected configurations.
   */
  public Optional<ConfigurationDto> getConfiguration(int id) {
    return configurationRepository.findById(id).map(Configuration::toDto);
  }

  public long countTotal() {
    return configurationRepository.count();
  }

  /**
   * Search for {@link Configuration} with a pagination.
   *
   * @param pageable The page requested
   * @return the list of corresponding {@link Configuration}
   */
  public List<Configuration> getPage(PageRequest pageable) {
    return configurationRepository.findAllByOrderByIdDesc(pageable).stream().toList();
  }
}
