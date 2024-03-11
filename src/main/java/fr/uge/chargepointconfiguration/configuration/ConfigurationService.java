package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.errors.exceptions.BadRequestException;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.shared.SearchUtils;
import jakarta.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
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
        firmwareRepository.findById(
            createConfigurationDto.firmware())
            .orElseThrow(() -> new EntityNotFoundException("Aucun firmware avec l'id "
                                                           + createConfigurationDto.firmware()))
    ));

    return configuration.toDto(); // TODO refacto for dealing with multiple entity<DTO>
  }

  /**
   * Update a configuration.
   *
   * @param id the id of the configuration to be updated
   * @param configurationDto All the necessary information for a configuration update.
   * @return A configuration created with its information.
   */
  public Configuration update(int id, CreateConfigurationDto configurationDto) {
    var configuration = configurationRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Aucune configuration avec l'id " + id));

    configuration.setName(configurationDto.name());
    configuration.setDescription(configurationDto.description());
    configuration.setConfiguration(configurationDto.configuration());
    configuration.setLastEdit(new Timestamp(System.currentTimeMillis()));

    configuration.setFirmware(
        firmwareRepository.findById(configurationDto.firmware())
            .orElseThrow(() -> new EntityNotFoundException("Aucun firmware avec l'id "
                                                           + configurationDto.firmware()))
    );
    return configurationRepository.save(configuration);
  }

  /**
   * Get all the configurations.
   *
   * @return A list of configurations.
   */
  public List<Configuration> getAllConfigurations() {
    return configurationRepository.findAllByOrderByIdDesc()
        .stream().toList();
  }

  /**
   * Get a configuration by id.
   *
   * @return Selected configurations.
   */
  public Configuration getConfiguration(int id) {
    return configurationRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Aucune configuration avec l'id " + id));
  }

  public long countTotal(String request) {
    try {
      var condition = SearchUtils.computeSpecification(request, Configuration.class);
      return configurationRepository.count(condition);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Requête invalide pour les filtres : " + request);
    }
  }

  /**
   * Search for {@link Configuration} with a pagination.
   *
   * @param request the request used to search
   * @param pageable The page requested
   * @return the list of corresponding {@link Configuration}
   */
  public List<Configuration> search(String request, PageRequest pageable) {
    try {
      var condition = SearchUtils.computeSpecification(request, Configuration.class);
      return configurationRepository.findAll(condition, pageable).stream().toList();
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Requête invalide pour les filtres : " + request);
    }
  }
}
