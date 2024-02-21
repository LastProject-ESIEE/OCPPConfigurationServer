package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.TechnicalLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for technical log.
 */
@Repository
public interface TechnicalLogRepository extends CrudRepository<TechnicalLog, Integer> {

}
