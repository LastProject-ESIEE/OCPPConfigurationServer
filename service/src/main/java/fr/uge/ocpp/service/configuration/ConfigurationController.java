package fr.uge.ocpp.service.configuration;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.ports.api.ConfigurationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The REST endpoints of the configuration API. */
@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

  private final ConfigurationService configurationService;

  /**
   * Creates the controller.
   *
   * @param configurationService the configuration use cases.
   */
  public ConfigurationController(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  /**
   * Returns all the configurations.
   *
   * @return all the configurations.
   */
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('VISUALIZER')")
  public List<ConfigurationDto> getAllConfiguration() {
    return configurationService.findAll().configurationList().stream()
        .map(ConfigurationDto::of).toList();
  }

  /**
   * Returns the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration.
   */
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VISUALIZER')")
  public ConfigurationDto getConfigurationById(@PathVariable long id) {
    return ConfigurationDto.of(configurationService.findById(id));
  }

  /**
   * Creates a configuration.
   *
   * @param createConfigurationDto the values of the configuration.
   * @return the created configuration.
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ConfigurationDto> create(
      @RequestBody CreateConfigurationDto createConfigurationDto) {
    Configuration configuration =
        configurationService.save(ConfigurationMapper.toConfiguration(createConfigurationDto));
    return new ResponseEntity<>(ConfigurationDto.of(configuration), HttpStatus.CREATED);
  }

  /**
   * Updates the configuration with the given identifier.
   *
   * @param id                    the identifier of the configuration.
   * @param createConfigurationDto the new values of the configuration.
   * @return the updated configuration.
   */
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ConfigurationDto> update(
      @PathVariable long id, @RequestBody CreateConfigurationDto createConfigurationDto) {
    Configuration configuration = configurationService.update(id,
        ConfigurationMapper.toConfiguration(createConfigurationDto));
    return new ResponseEntity<>(ConfigurationDto.of(configuration), HttpStatus.OK);
  }
}
