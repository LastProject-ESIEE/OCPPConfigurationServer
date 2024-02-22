package fr.uge.chargepointconfiguration.configuration;

/**
 * DTO to read configuration in database.
 *
 * @param id Database id of the configuration stored.
 * @param name How you want your configuration to be named.
 * @param description Brieve description of the meaining of this configuration.
 * @param configuration A JSON containing key and values for your configuration.
 */
public record ConfigurationDto(
    int id,
    String name,
    String description,
    String configuration) {
}
