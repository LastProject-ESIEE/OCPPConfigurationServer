package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointDto;
import fr.uge.chargepointconfiguration.status.StatusRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A ConfigurationService doing database manipulations.
 */
@Service
public class ConfigurationService {

  private final ConfigurationRepository configurationRepository;

  /**
   * ConfigurationService's constructor.
   *
   * @param configurationRepository A ConfigurationRepository accessing to database.
   */
  @Autowired
  public ConfigurationService(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
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
        createConfigurationDto.configuration()));
    // TODO create status and chargepoint

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

}