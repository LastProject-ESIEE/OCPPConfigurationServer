package fr.uge.chargepointconfiguration.chargepoint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the charge point.
 */
@Repository
public interface PageableChargepointRepository
      extends PagingAndSortingRepository<Chargepoint, Integer> {
  Page<Chargepoint> findAllByClientIdContainingIgnoreCase(Pageable pageable, String clientId);
}
