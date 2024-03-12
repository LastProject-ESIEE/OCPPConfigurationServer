package fr.uge.chargepointconfiguration.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.uge.chargepointconfiguration.errors.exceptions.BadRequestException;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import fr.uge.chargepointconfiguration.shared.SearchUtils;
import fr.uge.chargepointconfiguration.tools.JsonParser;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

  record ConfigurationJson(
      @JsonProperty("1") String lightIntensity,
      @JsonProperty("4") String localAuthList,
      @JsonProperty("5") String stationMaxCurrent,
      @JsonProperty("6") String stationPassword) {
  }

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
    if (createConfigurationDto.name().isBlank()) {
      throw new BadRequestException("Le titre est requis");
    }

    var firmware = firmwareRepository.findById(
            createConfigurationDto.firmware())
        .orElseThrow(
            () -> new EntityNotFoundException(
                "Aucun firmware avec l'id " + createConfigurationDto.firmware()
            )
        );

    checkerConfig(createConfigurationDto);

    var configuration = configurationRepository.save(new Configuration(
            createConfigurationDto.name(),
            createConfigurationDto.description(),
            createConfigurationDto.configuration(),
            firmware
        )
    );

    return configuration.toDto(); // TODO refacto for dealing with multiple entity<DTO>
  }

  private static void checkerConfig(CreateConfigurationDto createConfigurationDto) {
    var confJson = JsonParser.stringToObject(ConfigurationJson.class,
        createConfigurationDto.configuration());

    var transcriptorsById =
        Arrays.stream(ConfigurationTranscriptor.values()).collect(Collectors.toMap(
            ConfigurationTranscriptor::getId,
            e -> e)
        );

    Arrays.stream(confJson.getClass().getDeclaredFields())
        .forEach(field -> {
          try {
            if (field.get(confJson) == null) {
              return;
            }
          } catch (IllegalAccessException e) {
            throw new AssertionError(e);
          }
          String key = field.getAnnotation(JsonProperty.class).value();
          try {
            var idTranscriptor = Integer.parseInt(key);
            var valid = field.get(confJson).toString()
                .matches(transcriptorsById.get(idTranscriptor).getRegexRule());
            if (!valid) {
              throw new BadRequestException(
                  "Le champs \"" + transcriptorsById.get(idTranscriptor).getFullName()
                  + "\" ne respecte pas ses contraintes.");
            }
          } catch (NumberFormatException e) {
            throw new BadRequestException("La clé " + key + " n'existe pas");
          } catch (IllegalAccessException e) {
            throw new AssertionError(e);
          }
        });
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

    checkerConfig(configurationDto);

    configuration.setName(configurationDto.name());
    configuration.setDescription(configurationDto.description());
    configuration.setConfiguration(configurationDto.configuration());
    configuration.setLastEdit(LocalDateTime.now());

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

  /**
   * Count the number of entities with the constraint of the given request.
   *
   * @param request the request used to search
   * @return the amount of entities with the constraint of the given request
   */
  public long countTotalWithFilter(String request) {
    try {
      var condition = SearchUtils.computeSpecification(request, Configuration.class);
      return configurationRepository.count(condition);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Requête invalide pour les filtres : " + request);
    }
  }

  public long count() {
    return configurationRepository.count();
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
