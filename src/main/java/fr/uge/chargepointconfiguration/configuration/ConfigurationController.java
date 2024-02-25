package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.chargepoint.ChargepointService;
import fr.uge.chargepointconfiguration.chargepoint.CreateChargepointDto;
import fr.uge.chargepointconfiguration.status.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the Configuration entity.
 */
@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

  private final ChargepointService chargepointService;

  private final ConfigurationService configurationService;

  private final StatusService statusService;

  /**
   * ConfigurationController's constructor.
   *
   * @param configurationService A ConfigurationService doing database manipulations.
   */
  @Autowired
  public ConfigurationController(ChargepointService chargepointService,
                                 ConfigurationService configurationService,
                                 StatusService statusService) {
    this.chargepointService = chargepointService;
    this.configurationService = configurationService;
    this.statusService = statusService;
  }

  /**
   * Returns a list of all configuration without the configuration.
   *
   * @return A list of all the configuration.
   */
  @Operation(summary = "Get all configuration")
  @ApiResponse(responseCode = "200",
      description = "Found all the configuration",
      content = { @Content(mediaType = "application/json",
          schema = @Schema(implementation = ConfigurationGeneralDto.class))
      })
  @GetMapping(value = "/all")
  public List<ConfigurationGeneralDto> getAllConfiguration() {
    return configurationService.getAllConfigurations();
  }

  /**
   * Create a configuration.
   *
   * @param createConfigurationDto All the necessary information for a configuration creation.
   * @return A configuration created with its information and its http result status.
   */
  @Operation(summary = "Post a configuration")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Configuration created",
          content = { @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ConfigurationDto.class)) }),
      @ApiResponse(responseCode = "409",
          description = "Configuration creation has a conflict",
          content = @Content)
  })
  @PostMapping("/create")
  public ResponseEntity<ConfigurationDto> registerConfiguration(
      @Parameter(
          name = "CreateConfigurationDto",
          description = "Valid createConfiguration form",
          example = """
              {  "name": "String",
                "description": "String",
                "configuration": [
                  {
                    "key": "String",
                    "value": "String"
                  }
                ],
                "firmware": "String"
              }""",
          required = true)
      @RequestBody CreateConfigurationDto createConfigurationDto) {
    var status = statusService.save();
    var configuration = configurationService.save(createConfigurationDto);
    chargepointService.save(new CreateChargepointDto(
        "ACE0272306",
        "Eve Single S-line",
        "Alfen BV",
        "BRS",
        "www.brs-prod.com",
        configuration.id(),
        status.id()
    ));
    return new ResponseEntity<>(configuration,
        HttpStatus.CREATED);
  }
}