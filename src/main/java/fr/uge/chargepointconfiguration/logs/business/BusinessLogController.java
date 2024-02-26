package fr.uge.chargepointconfiguration.logs.business;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for business log.
 */
@RequestMapping("/api/log/business")
@RestController
public class BusinessLogController {

  private final BusinessLogService businessLogService;

  /**
   * BusinessLogController's constructor.
   *
   * @param businessLogService A BusinessLogService.
   */
  @Autowired
  public BusinessLogController(BusinessLogService businessLogService) {
    this.businessLogService = businessLogService;
  }

  /**
   * Returns a list of business logs according to the given chargepoint.
   *
   * @param id the id of the chargepoint.
   * @return a list of business logs by chargepoint.
   */
  @Operation(summary = "Get a list of logs by its charge point")
  @ApiResponse(responseCode = "200",
          description = "Found the list of business logs",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = BusinessLog.class))
          })
  @GetMapping(value = "/{id}")
  public List<BusinessLog> getBusinessLogByChargepointId(@PathVariable int id) {
    return businessLogService.getAllByChargepointId(id);
  }
}
