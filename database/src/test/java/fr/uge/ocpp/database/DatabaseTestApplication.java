package fr.uge.ocpp.database;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Minimal Spring Boot configuration for the database module tests: {@code @DataJpaTest} looks for a
 * {@link SpringBootConfiguration} in the test classpath to bootstrap the JPA slice, and entity and
 * repository scanning default to this class' package ({@code fr.uge.ocpp.database}). {@link
 * EnableAutoConfiguration} is required so the auto-configuration base package is registered.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
public class DatabaseTestApplication {}
