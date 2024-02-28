package fr.uge.chargepointconfiguration.logs.business;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLog;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the business log.
 */
@Repository
public interface BusinessLogRepository extends CrudRepository<BusinessLog, Integer>,
      PagingAndSortingRepository<BusinessLog, Integer> {

  /**
   * Method to return all business logs.
   *
   * @return a list of all business logs.
   */
  List<BusinessLog> findAll();

  Page<BusinessLog> findAll(Pageable pageable);

  List<BusinessLog> findAllByChargepoint(Chargepoint chargepointId);
}
