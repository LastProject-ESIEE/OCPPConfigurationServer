package fr.uge.ocpp.service.configuration;

/** The values required to create or update a configuration. */
public record CreateConfigurationDto(String name,
                                     String description,
                                     String configuration,
                                     long firmware) {
}
