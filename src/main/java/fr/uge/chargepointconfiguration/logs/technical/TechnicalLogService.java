package fr.uge.chargepointconfiguration.logs.technical;

import java.util.List;
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

  public List<TechnicalLog>  getTechnicalLogByComponentAndCriticality(
      TechnicalLog.Component component,
      TechnicalLog.Criticality criticality) {
    return technicalLogRepository.findAllByComponentAndCriticality(component, criticality);
  }
}
