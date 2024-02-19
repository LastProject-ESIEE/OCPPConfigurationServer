package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.Chargepoint;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import io.swagger.v3.oas.annotations.Operation;
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
 * A controller for the Chargepoint entity.
 */
@RestController
public class ChargepointController {

  private final ChargepointRepository chargepointRepository;

  /**
   * ChargepointController's constructor.
   *
   * @param chargepointRepository A ChargePointRepository.
   */
  @Autowired
  public ChargepointController(ChargepointRepository chargepointRepository) {
    this.chargepointRepository = chargepointRepository;
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
  @GetMapping(value = "/chargepoint/all")
  public List<Chargepoint> getAllChargepoints() {
    return StreamSupport.stream(chargepointRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
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
  @GetMapping(value = "/chargepoint/{id}")
  public Optional<Chargepoint> getChargepointById(@PathVariable int id) {
    return chargepointRepository.findById(id);
  }
}