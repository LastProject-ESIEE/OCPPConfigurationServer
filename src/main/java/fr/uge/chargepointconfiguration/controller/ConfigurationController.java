package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.Chargepoint;
import fr.uge.chargepointconfiguration.entities.Configuration;
import fr.uge.chargepointconfiguration.entities.dto.ConfigurationGeneralDto;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import fr.uge.chargepointconfiguration.repository.ConfigurationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * A controller for the Configuration entity.
 */
@RestController
public class ConfigurationController {

  private final ConfigurationRepository configurationRepository;

  /**
   * Configuration controller's constructor.
   *
   * @param configurationRepository configuration repository.
   */
  @Autowired
  public ConfigurationController(ConfigurationRepository configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  /**
   * Returns a list of all configuration.
   *
   * @return A list of all the configuration.
   */
  @Operation(summary = "Get all configuration")
  @ApiResponse(responseCode = "200",
          description = "Found all the configuration",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ConfigurationGeneralDto.class))
          })
  @GetMapping(value = "/configuration/all")
  public List<ConfigurationGeneralDto> getAllConfiguration() {
    return configurationRepository.findAll().stream().map(ConfigurationGeneralDto::from).toList();
  }

}