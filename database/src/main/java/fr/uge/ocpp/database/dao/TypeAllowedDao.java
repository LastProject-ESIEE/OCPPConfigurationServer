package fr.uge.ocpp.database.dao;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data access to the allowed type table. */
public interface TypeAllowedDao extends JpaRepository<TypeAllowedEntity, Long> {}
