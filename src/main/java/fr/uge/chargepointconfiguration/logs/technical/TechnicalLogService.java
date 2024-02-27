package fr.uge.chargepointconfiguration.logs.technical;

import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A TechnicalLogService doing database manipulations.
 */
@Service
public class TechnicalLogService {

  private final TechnicalLogRepository technicalLogRepository;

  @Autowired
  public TechnicalLogService(TechnicalLogRepository technicalLogRepository) {
    this.technicalLogRepository = technicalLogRepository;
  }

  public List<TechnicalLog>  getTechnicalLogByComponentAndLevel(
      TechnicalLog.Component component,
      Level level) {
    return technicalLogRepository.findAllByComponentAndLevel(component, level);
  }
}
