package fr.uge.chargepointconfiguration.logs.technical;

import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLogEntity;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

  public List<TechnicalLogEntity>  getTechnicalLogByComponentAndLevel(
      TechnicalLogEntity.Component component,
      Level level) {
    return technicalLogRepository.findAllByComponentAndLevel(component, level.name());
  }

  public long countTotal() {
    return technicalLogRepository.count();
  }

  /**
   * Search for {@link TechnicalLogEntity} with a pagination.
   *
   * @param pageable         The page requested
   * @return the list of corresponding {@link TechnicalLogEntity}
   */
  public List<TechnicalLogEntity> getPage(PageRequest pageable) {
    return technicalLogRepository.findAll(pageable)
          .stream().toList();
  }
}
