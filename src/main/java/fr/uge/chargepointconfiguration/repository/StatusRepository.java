package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the charge point.
 */
@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
}
