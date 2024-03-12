package fr.uge.chargepointconfiguration.typeallowed;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.firmware.Firmware;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for type allowed.
 */
public interface TypeAllowedRepository extends CrudRepository<TypeAllowed, Integer> {
  List<TypeAllowed> findAll();
}
