package fr.uge.chargepointconfiguration.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the configuration entity.
 */
@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

  private final ConfigurationService configurationService;

  /**
   * ConfigurationController's constructor.
   *
   * @param configurationService A ConfigurationService doing database manipulations.
   */
  @Autowired
  public ConfigurationController(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  /**
   * Create a configuration.
   *
   * @param createConfigurationDto All the necessary information for a configuration creation.
   * @return A configuration created with its information and its http result status.
   */
  @PostMapping("/create")
  public ResponseEntity<ConfigurationDto> registerConfiguration(
      @RequestBody CreateConfigurationDto createConfigurationDto) {
    return new ResponseEntity<>(configurationService.save(createConfigurationDto),
        HttpStatus.CREATED);
  }
}
