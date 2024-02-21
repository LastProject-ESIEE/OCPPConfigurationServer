package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.BusinessLog;
import fr.uge.chargepointconfiguration.repository.BusinessLogRepository;
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
 * Controller for business log.
 */
@RestController
public class BusinessLogController {

  private final BusinessLogRepository businessLogRepository;

  /**
   * BusinessLogController's constructor.
   *
   * @param businessLogRepository A BusinessLogRepository.
   */
  @Autowired
  public BusinessLogController(BusinessLogRepository businessLogRepository) {
    this.businessLogRepository = businessLogRepository;
  }

  /**
   * Save the business log in the database.
   *
   * @param businessLog a BusinessLog.
   */
  public void addBusinessLog(BusinessLog businessLog) {
    Objects.requireNonNull(businessLog);
    businessLogRepository.save(businessLog);
  }

  /**
   * Returns a list of business logs according to the given charge point.
   *
   * @param chargepoint the id of the charge point.
   * @return a list of business logs by charge point.
   */
  @Operation(summary = "Get a list of logs by its charge point")
  @ApiResponse(responseCode = "200",
          description = "Found the list of business logs",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = BusinessLog.class))
          })
  @GetMapping(value = "/log/business/{chargepoint}")
  public List<BusinessLog> getBusinessLogByChargepoint(@PathVariable int chargepoint) {
    return businessLogRepository.findAllByChargePoint(chargepoint);
  }
}
