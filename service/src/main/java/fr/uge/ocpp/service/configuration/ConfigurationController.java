package fr.uge.ocpp.service.configuration;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.ports.api.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Configuration", description = "The configuration API")
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
  @Operation(summary = "Get all the configurations")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found all the configurations",
            content =
                @Content(
                    mediaType = "application/json",
                    array =
                        @ArraySchema(schema = @Schema(implementation = ConfigurationDto.class)))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the VISUALIZER role")
      })
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('VISUALIZER')")
  public List<ConfigurationDto> getAllConfiguration() {
    return configurationService.findAll().configurationList().stream()
        .map(ConfigurationDto::of)
        .toList();
  }

  /**
   * Returns the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration.
   */
  @Operation(summary = "Get a configuration by its id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the corresponding configuration",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConfigurationDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the VISUALIZER role"),
        @ApiResponse(responseCode = "404", description = "No configuration with this id")
      })
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VISUALIZER')")
  public ConfigurationDto getConfigurationById(
      @Parameter(description = "Id of the configuration to look for") @PathVariable long id) {
    return ConfigurationDto.of(configurationService.findById(id));
  }

  /**
   * Creates a configuration.
   *
   * @param createConfigurationDto the values of the configuration.
   * @return the created configuration.
   */
  @Operation(summary = "Create a configuration")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "The configuration was created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConfigurationDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid configuration values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(
            responseCode = "409",
            description = "A configuration with this name already exists")
      })
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
   * @param id the identifier of the configuration.
   * @param createConfigurationDto the new values of the configuration.
   * @return the updated configuration.
   */
  @Operation(summary = "Update a configuration")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The configuration was updated",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ConfigurationDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid configuration values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(responseCode = "404", description = "No configuration with this id"),
        @ApiResponse(
            responseCode = "409",
            description = "A configuration with this name already exists")
      })
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ConfigurationDto> update(
      @Parameter(description = "Id of the configuration to update") @PathVariable long id,
      @RequestBody CreateConfigurationDto createConfigurationDto) {
    Configuration configuration =
        configurationService.update(
            id, ConfigurationMapper.toConfiguration(createConfigurationDto));
    return new ResponseEntity<>(ConfigurationDto.of(configuration), HttpStatus.OK);
  }
}
