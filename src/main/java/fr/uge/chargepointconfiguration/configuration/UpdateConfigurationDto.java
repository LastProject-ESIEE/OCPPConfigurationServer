package fr.uge.chargepointconfiguration.configuration;

/**
 * DTO for configuration update in database.
 *
 * @param name New configuration name.
 * @param description New configuration description.
 * @param configuration A JSON containing key and values for the configuration.
 * @param firmware An int referencing the id of firmware for the configuration.
 */
public record UpdateConfigurationDto(String name,
                                     String description,
                                     String configuration,
                                     int firmware) {
}
