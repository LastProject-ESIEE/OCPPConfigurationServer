package fr.uge.chargepointconfiguration.chargepoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * A controller for the Chargepoint entity.
 */
@RequestMapping("/api/chargepoint")
@RestController
public class ChargepointController {

  private final ChargepointService chargepointService;

  /**
   * ChargepointController's constructor.
   *
   * @param chargepointService A ChargePointService.
   */
  @Autowired
  public ChargepointController(ChargepointService chargepointService) {
    this.chargepointService = chargepointService;
  }

  /**
   * Returns a list of all the chargepoint.
   *
   * @return A list of all the chargepoint.
   */
  @Operation(summary = "Get all the chargepoints")
  @ApiResponse(responseCode = "200",
          description = "Found all the chargepoints",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Chargepoint.class))
          })
  @GetMapping(value = "/all")
  public List<ChargepointDto> getAllChargepoints() {
    return chargepointService.getAllChargepoints();
  }

  /**
   * Returns an optional of chargepoint according to the given id.<br>
   * It is empty if the repository could not find a chargepoint.
   *
   * @param id An int.
   * @return An optional of chargepoint.
   */
  @Operation(summary = "Get a chargepoint by its id")
  @ApiResponses(value = { @ApiResponse(responseCode = "200",
          description = "Found the chargepoint",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Chargepoint.class)) }),
                          @ApiResponse(responseCode = "404",
                  description = "This chargepoint does not exist",
                  content = @Content) })
  @GetMapping(value = "/{id}")
  public Optional<ChargepointDto> getChargepointById(@PathVariable int id) {
    return chargepointService.getChargepointById(id);
  }

  /**
   * Returns a list of all the chargepoints.
   *
   * @return A list corresponding chargepoints.
   */
  @Operation(summary = "Search for chargepoints")
  @ApiResponse(responseCode = "200",
        description = "Search for chargepoints",
        content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = Chargepoint.class))
        })
  @GetMapping(value = "/search")
  public List<ChargepointDto> searchChargepoints(
        @RequestParam int size,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "") String clientIdContains,
        @RequestParam(required = false, defaultValue = "id") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String order
  ) {
    return chargepointService.search(
          PageRequest.of(page, size, Sort.by(Sort.Order.by(order).getDirection(), sortBy)),
          clientIdContains
    );
  }

}