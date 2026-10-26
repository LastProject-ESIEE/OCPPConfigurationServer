package fr.uge.ocpp.database.repositories;

import fr.uge.ocpp.database.entities.ConfigurationEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Spring Data access to the configuration table. */
@Repository
public interface ConfigurationJpaRepository
    extends JpaRepository<ConfigurationEntity, Long>,
        JpaSpecificationExecutor<ConfigurationEntity> {

  /**
   * Finds all the configurations ordered by descending identifier.
   *
   * @return all the configurations.
   */
  List<ConfigurationEntity> findAllByOrderByIdDesc();

  /**
   * Finds all the configurations ordered by descending identifier, paginated.
   *
   * @param pageable the pagination information.
   * @return the corresponding page of configurations.
   */
  Page<ConfigurationEntity> findAllByOrderByIdDesc(Pageable pageable);
}
