package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.BusinessLog;
import fr.uge.chargepointconfiguration.repository.BusinessLogRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
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
}
