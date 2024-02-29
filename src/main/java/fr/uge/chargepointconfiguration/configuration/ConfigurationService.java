package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
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
   * @param firmwareRepository A FirmwareRepository accessing to database.
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
   * Get all the configurations.
   *
   * @return A list of configurations.
   */
  public List<ConfigurationGeneralDto> getAllConfigurations() {
    return configurationRepository.findAll().stream().map(ConfigurationGeneralDto::from).toList();
  }

  public Optional<ConfigurationDto> getConfigurationById(int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    return Optional.of(configurationRepository.findById(id).orElseThrow().toDto());
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
    return configurationRepository.findAll(pageable).stream().toList();
  }
}
