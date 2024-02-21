package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.TechnicalLog;
import fr.uge.chargepointconfiguration.repository.TechnicalLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for technical log.
 */
@RestController
public class TechnicalLogController {

  private final TechnicalLogRepository technicalLogRepository;

  /**
   * TechnicalLogController's constructor.
   *
   * @param technicalLogRepository a TechnicalLogRepository.
   */
  @Autowired
  public TechnicalLogController(TechnicalLogRepository technicalLogRepository) {
    this.technicalLogRepository = technicalLogRepository;
  }

  /**
   * Returns a list of technical logs according to the given component and criticality.
   *
   * @param component   a type of component of the system.
   * @param criticality a critical level of the log.
   * @return a list of technical logs by component and criticality.
   */
  @Operation(summary = "Get a list of logs by its component and criticality")
  @ApiResponse(responseCode = "200",
          description = "Found the list of technical logs",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = TechnicalLog.class))
          })
  @GetMapping(value = "/log/technical/{component}/{criticality}")
  public List<TechnicalLog> getTechnicalLogByComponentAndCriticality(
          @PathVariable TechnicalLog.Component component,
          @PathVariable TechnicalLog.Criticality criticality) {
    return technicalLogRepository.findAllByComponentAndCriticality(component, criticality);
  }
}
