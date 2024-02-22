package fr.uge.chargepointconfiguration.status;

import fr.uge.chargepointconfiguration.status.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the charge point.
 */
@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
}
