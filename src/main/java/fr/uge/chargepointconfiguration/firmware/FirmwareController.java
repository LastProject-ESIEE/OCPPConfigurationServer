package fr.uge.chargepointconfiguration.firmware;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the firmware entity.
 */
@RequestMapping("/api/firmware")
@RestController
@Tag(name = "Firmware", description = "The firmware API")
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
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = Firmware.class)
      )
  )
  @GetMapping(value = "/all")
  public List<FirmwareDto> getAllFirmwares() {
    return firmwareRepository.findAll().stream().map(Firmware::toDto).toList();
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
      @ApiResponse(
            responseCode = "200",
            description = "Found the firmware",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Firmware.class)
            )
      ),
      @ApiResponse(
            responseCode = "404",
            description = "This firmware does not exist"
      )
  })
  @GetMapping(value = "/{id}")
  public Optional<FirmwareDto> getFirmwareById(
      @Parameter(description = "id of firmware to be searched") @PathVariable int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    return Optional.of(firmwareRepository.findById(id).orElseThrow().toDto());
  }
}
