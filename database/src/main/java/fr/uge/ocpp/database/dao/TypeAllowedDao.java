package fr.uge.ocpp.database.dao;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data access to the allowed type table. */
@Repository
public interface TypeAllowedDao extends JpaRepository<TypeAllowedEntity, Long> {}
