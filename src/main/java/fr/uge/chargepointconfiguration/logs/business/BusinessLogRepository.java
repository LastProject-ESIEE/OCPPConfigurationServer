package fr.uge.chargepointconfiguration.logs.business;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLog;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the business log.
 */
@Repository
public interface BusinessLogRepository extends CrudRepository<BusinessLog, Integer> {

  /**
   * Method to return all business logs.
   *
   * @return a list of all business logs.
   */
  List<BusinessLog> findAll();

  List<BusinessLog> findAllByChargepointId(Chargepoint chargepointId);
}
