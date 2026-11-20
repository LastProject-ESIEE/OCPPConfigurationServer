package fr.uge.ocpp.database.dao;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data access to the allowed type table. */
public interface TypeAllowedDao extends JpaRepository<TypeAllowedEntity, Long> {

  /**
   * Finds all the allowed types ordered by descending identifier, paginated.
   *
   * @param pageable the pagination information.
   * @return the corresponding page of allowed types.
   */
  Page<TypeAllowedEntity> findAllByOrderByIdDesc(Pageable pageable);
}
