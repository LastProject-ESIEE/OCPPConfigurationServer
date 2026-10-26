package fr.uge.ocpp.database.repositories;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data access to the allowed type table. */
@Repository
public interface TypeAllowedJpaRepository extends JpaRepository<TypeAllowedEntity, Long> {
}
