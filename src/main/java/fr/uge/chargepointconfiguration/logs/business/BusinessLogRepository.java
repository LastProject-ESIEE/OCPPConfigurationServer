package fr.uge.chargepointconfiguration.logs.business;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.logs.sealed.BusinessLogEntity;
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
public interface BusinessLogRepository extends CrudRepository<BusinessLogEntity, Integer>,
      PagingAndSortingRepository<BusinessLogEntity, Integer> {

  /**
   * Method to return all business logs.
   *
   * @return a list of all business logs.
   */
  List<BusinessLogEntity> findAll();

  Page<BusinessLogEntity> findAll(Pageable pageable);

  List<BusinessLogEntity> findAllByChargepoint(Chargepoint chargepointId);
}
