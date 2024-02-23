package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.firmware.Firmware;
import fr.uge.chargepointconfiguration.status.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

  private final StatusService statusService;

  /**
   * ConfigurationController's constructor.
   *
   * @param configurationService A ConfigurationService doing database manipulations.
   */
  @Autowired
  public ConfigurationController(ConfigurationService configurationService,
                                 StatusService statusService) {
    this.configurationService = configurationService;
    this.statusService = statusService;
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

    return new ResponseEntity<>(configuration,
        HttpStatus.CREATED);
  }
}
