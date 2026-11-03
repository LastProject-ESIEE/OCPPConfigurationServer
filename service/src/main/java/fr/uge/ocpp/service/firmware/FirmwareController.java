package fr.uge.ocpp.service.firmware;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.ports.api.FirmwareService;
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

/** The REST endpoints of the firmware API. */
@RestController
@RequestMapping("/api/firmware")
@Tag(name = "Firmware", description = "The firmware API")
public class FirmwareController {

  private final FirmwareService firmwareService;

  /**
   * Creates the controller.
   *
   * @param firmwareService the firmware use cases.
   */
  public FirmwareController(FirmwareService firmwareService) {
    this.firmwareService = firmwareService;
  }

  /**
   * Returns all the firmwares.
   *
   * @return all the firmwares.
   */
  @Operation(summary = "Get all the firmwares")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found all the firmwares",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = FirmwareDto.class)))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the VISUALIZER role")
      })
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('VISUALIZER')")
  public List<FirmwareDto> getAllFirmwares() {
    return firmwareService.findAll().firmwareList().stream().map(FirmwareDto::of).toList();
  }

  /**
   * Returns the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware.
   */
  @Operation(summary = "Get a firmware by its id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the corresponding firmware",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FirmwareDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the VISUALIZER role"),
        @ApiResponse(responseCode = "404", description = "No firmware with this id")
      })
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VISUALIZER')")
  public FirmwareDto getFirmwareById(
      @Parameter(description = "Id of the firmware to look for") @PathVariable long id) {
    return FirmwareDto.of(firmwareService.findById(id));
  }

  /**
   * Creates a firmware.
   *
   * @param createFirmwareDto the values of the firmware.
   * @return the created firmware.
   */
  @Operation(summary = "Create a firmware")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "The firmware was created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FirmwareDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid firmware values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(responseCode = "409", description = "A firmware with this url already exists")
      })
  @PostMapping("/create")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<FirmwareDto> create(@RequestBody CreateFirmwareDto createFirmwareDto) {
    Firmware firmware = firmwareService.save(FirmwareMapper.toFirmware(null, createFirmwareDto));
    return new ResponseEntity<>(FirmwareDto.of(firmware), HttpStatus.CREATED);
  }

  /**
   * Updates the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @param createFirmwareDto the new values of the firmware.
   * @return the updated firmware.
   */
  @Operation(summary = "Update a firmware")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The firmware was updated",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FirmwareDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid firmware values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(responseCode = "404", description = "No firmware with this id"),
        @ApiResponse(responseCode = "409", description = "A firmware with this url already exists")
      })
  @PatchMapping("/update/{id}")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<FirmwareDto> update(
      @Parameter(description = "Id of the firmware to update") @PathVariable long id,
      @RequestBody CreateFirmwareDto createFirmwareDto) {
    Firmware firmware =
        firmwareService.update(id, FirmwareMapper.toFirmware(id, createFirmwareDto));
    return new ResponseEntity<>(FirmwareDto.of(firmware), HttpStatus.OK);
  }
}
