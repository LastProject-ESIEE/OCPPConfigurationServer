package fr.uge.chargepointconfiguration.logs.business;

import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLog;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * A BusinessLogService doing database manipulations.
 */
@Service
public class BusinessLogService {

  private final BusinessLogRepository businessLogRepository;

  private final ChargepointRepository chargepointRepository;

  /**
   * BusinessLogService's constructor.
   *
   * @param businessLogRepository A BusinessLogRepository accessing to database.
   * @param chargepointRepository A ChargepointRepository accessing to database.
   */
  @Autowired
  public BusinessLogService(BusinessLogRepository businessLogRepository,
                            ChargepointRepository chargepointRepository) {
    this.businessLogRepository = businessLogRepository;
    this.chargepointRepository = chargepointRepository;
  }

  /**
   * Returns a list of business logs filtered by a chargepoint id.
   *
   * @param chargepointId the id of the chargepoint.
   * @return a list of business logs by chargepoint.
   */
  public List<BusinessLog> getAllByChargepointId(int chargepointId) {
    var chargepoint = chargepointRepository.findById(chargepointId).orElseThrow();
    // TODO : exception BAD REQUEST si id est pas un nombre
    return businessLogRepository.findAllByChargepoint(chargepoint);
  }

  public long countTotal() {
    return businessLogRepository.count();
  }

  /**
   * Search for {@link BusinessLogDto} with a pagination.
   *
   * @param pageable The page requested
   * @return the list of corresponding {@link BusinessLogDto}
   */
  public List<BusinessLogDto> getPage(PageRequest pageable) {
    return businessLogRepository.findAll(pageable)
          .stream()
          .map(log -> new BusinessLogDto(log.getId(),
                log.getDate(),
                log.getUser(),
                log.getChargepoint(),
                log.getCategory(),
                log.getLevel(),
                log.getCompleteLog()))
          .toList();
  }
}