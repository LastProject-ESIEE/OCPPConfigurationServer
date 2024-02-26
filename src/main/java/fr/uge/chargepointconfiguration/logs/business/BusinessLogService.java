package fr.uge.chargepointconfiguration.logs.business;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A BusinessLog doing database manipulations.
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
   * Returns a list of business logs according to the given chargepoint.
   *
   * @param chargepointId the id of the chargepoint.
   * @return a list of business logs by chargepoint.
   */
  public List<BusinessLog> getAllByChargepointId(int chargepointId) {
    var chargepoint = chargepointRepository.findById(chargepointId).orElseThrow();
    // TODO : exception BAD REQUEST si id est pas un nombre
    return businessLogRepository.findAllByChargepointId(chargepoint);
  }
}
