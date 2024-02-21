package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.BusinessLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the business log.
 */
@Repository
public interface BusinessLogRepository extends CrudRepository<BusinessLog, Integer> {
}
