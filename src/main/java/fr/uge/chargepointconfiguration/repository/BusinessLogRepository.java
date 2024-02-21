package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.BusinessLog;
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

  List<BusinessLog> findAllByChargePoint(int chargePoint);

  List<BusinessLog> findAllByFirmwareVersion(String firmwareVersion);
}
