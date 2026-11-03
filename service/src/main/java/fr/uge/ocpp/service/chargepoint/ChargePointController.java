package fr.uge.ocpp.service.chargepoint;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
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

/** The REST endpoints of the charge point API. */
@RestController
@RequestMapping("/api/chargepoint")
@Tag(name = "Charge point", description = "The charge point API")
public class ChargePointController {

  private final ChargePointService chargePointService;

  /**
   * Creates the controller.
   *
   * @param chargePointService the charge point use cases.
   */
  public ChargePointController(ChargePointService chargePointService) {
    this.chargePointService = chargePointService;
  }

  /**
   * Returns all the charge points.
   *
   * @return all the charge points.
   */
  @Operation(summary = "Get all the charge points")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found all the charge points",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ChargePointDto.class)))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the VISUALIZER role")
      })
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('VISUALIZER')")
  public List<ChargePointDto> getAllChargepoints() {
    return chargePointService.findAll().chargePointList().stream().map(ChargePointDto::of).toList();
  }

  /**
   * Returns the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @return the corresponding charge point.
   */
  @Operation(summary = "Get a charge point by its id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found the corresponding charge point",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChargePointDto.class))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the VISUALIZER role"),
        @ApiResponse(responseCode = "404", description = "No charge point with this id")
      })
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VISUALIZER')")
  public ChargePointDto getChargepointById(
      @Parameter(description = "Id of the charge point to look for") @PathVariable long id) {
    return ChargePointDto.of(chargePointService.findById(id));
  }

  /**
   * Creates a charge point.
   *
   * @param createChargePointDto the values of the charge point.
   * @return the created charge point.
   */
  @Operation(summary = "Create a charge point")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "The charge point was created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChargePointDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid charge point values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(
            responseCode = "409",
            description = "A charge point with this serial number already exists")
      })
  @PostMapping("/create")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ChargePointDto> create(
      @RequestBody CreateChargePointDto createChargePointDto) {
    ChargePoint chargePoint =
        chargePointService.save(ChargePointMapper.toChargePoint(createChargePointDto));
    return new ResponseEntity<>(ChargePointDto.of(chargePoint), HttpStatus.CREATED);
  }

  /**
   * Updates the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @param createChargePointDto the new values of the charge point.
   * @return the updated charge point.
   */
  @Operation(summary = "Update a charge point")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "The charge point was updated",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChargePointDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid charge point values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(responseCode = "404", description = "No charge point with this id"),
        @ApiResponse(
            responseCode = "409",
            description = "A charge point with this serial number already exists")
      })
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ChargePointDto> update(
      @Parameter(description = "Id of the charge point to update") @PathVariable long id,
      @RequestBody CreateChargePointDto createChargePointDto) {
    ChargePoint chargePoint =
        chargePointService.update(id, ChargePointMapper.toChargePoint(createChargePointDto));
    return new ResponseEntity<>(ChargePointDto.of(chargePoint), HttpStatus.OK);
  }
}
