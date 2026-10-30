package fr.uge.ocpp.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Scans the entities and Spring Data repositories of the database module.
 *
 * <p>This wiring lives in a dedicated configuration instead of the application class so that web
 * slice tests do not require a datasource.
 */
@Configuration
@EntityScan(basePackages = "fr.uge.ocpp.database.entities")
@EnableJpaRepositories(basePackages = "fr.uge.ocpp.database.repositories")
public class DatabaseConfig {}
