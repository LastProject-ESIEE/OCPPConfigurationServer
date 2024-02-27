package fr.uge.chargepointconfiguration.logs;

import fr.uge.chargepointconfiguration.logs.business.BusinessLogRepository;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLog;
import fr.uge.chargepointconfiguration.logs.sealed.Log;
import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import fr.uge.chargepointconfiguration.logs.technical.TechnicalLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A CustomLoggerService doing database manipulations.
 */
@Service
public class CustomLoggerService {

  private final BusinessLogRepository businessLogRepository;

  private final TechnicalLogRepository technicalLogRepository;

  /**
   * CustomLoggerService's constructor.
   *
   * @param businessLogRepository A BusinessLogRepository accessing to database.
   * @param technicalLogRepository A TechnicalLogRepository accessing to database.
   */
  @Autowired
  public CustomLoggerService(BusinessLogRepository businessLogRepository,
                              TechnicalLogRepository technicalLogRepository) {
    this.businessLogRepository = businessLogRepository;
    this.technicalLogRepository = technicalLogRepository;
  }

  /**
   * Create a log.
   *
   * @param log A log saved in database.
   * @return The used log.
   */
  public Log save(Log log) {
    return switch (log) {
      case BusinessLog businessLog -> businessLogRepository.save(businessLog);
      case TechnicalLog technicalLog -> technicalLogRepository.save(technicalLog);
    };
  }
}
