package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Configuration;
import fr.uge.chargepointconfiguration.entities.Status;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the charge point configuration.
 */
@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, Integer> {
  List<Configuration> findAll();
}
