package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.TechnicalLog;
import fr.uge.chargepointconfiguration.repository.TechnicalLogRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
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
   * Save the technical log in the database.
   *
   * @param technicalLog a TechnicalLog.
   */
  public void addTechnicalLog(TechnicalLog technicalLog) {
    Objects.requireNonNull(technicalLog);
    technicalLogRepository.save(technicalLog);
  }
}
