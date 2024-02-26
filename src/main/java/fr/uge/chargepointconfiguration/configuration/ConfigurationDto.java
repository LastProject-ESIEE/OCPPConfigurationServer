package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.firmware.Firmware;
import java.sql.Timestamp;

/**
 * DTO to read configuration in database.
 *
 * @param id Database id of the configuration stored.
 * @param name How you want your configuration to be named.
 * @param description Brieve description of the meaining of this configuration.
 * @param configuration A JSON containing key and values for your configuration.
 * @param firmware {@link Firmware}.
 */
public record ConfigurationDto(
    int id,
    String name,
    String description,
    Timestamp lastEdit,
    String configuration,
    Firmware firmware) {
}
