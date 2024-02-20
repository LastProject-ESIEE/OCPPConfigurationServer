package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.Firmware;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the firmware entity.
 */
@RestController
public class FirmwareController {

  private final FirmwareRepository firmwareRepository;

  /**
   * FirmwareController's constructor.
   *
   * @param firmwareRepository A FirmwareRepository.
   */
  @Autowired
  public FirmwareController(FirmwareRepository firmwareRepository) {
    this.firmwareRepository = firmwareRepository;
  }

  /**
   * Returns all the firmwares.
   *
   * @return A list of all the firmwares.
   */
  @Operation(summary = "Get all the firmwares")
  @ApiResponse(responseCode = "200",
      description = "Found all the firmwares",
      content = { @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = Firmware.class)) })
  @GetMapping(value = "/firmware/all")
  public List<Firmware> getAllFirmwares() {
    return firmwareRepository.findAll();
  }

  /**
   * Returns a firmware according to the given id.<br>
   * The optional will be empty if a firmware could not be found.
   *
   * @param id An int.
   * @return An optional of firmware.
   */
  @Operation(summary = "Get a firmware by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Found the firmware",
          content = { @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = Firmware.class)) }),
      @ApiResponse(responseCode = "404",
          description = "This firmware does not exist",
          content = @Content)
  })
  @GetMapping(value = "/firmware/{id}")
  public Optional<Firmware> getFirmwareById(
      @Parameter(description = "id of firmware to be searched") @PathVariable int id) {
    return firmwareRepository.findById(id);
  }
}
