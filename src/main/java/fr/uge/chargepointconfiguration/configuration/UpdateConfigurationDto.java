package fr.uge.chargepointconfiguration.configuration;

/**
 * DTO for configuration update in database.
 *
 * @param id Id of the configuration to be updated.
 * @param name New configuration name.
 * @param description New configuration description.
 * @param configuration A JSON containing key and values for the configuration.
 */
public record UpdateConfigurationDto(int id,
                                     String name,
                                     String description,
                                     String configuration,
                                     int firmware) {
}
