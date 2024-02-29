package fr.uge.chargepointconfiguration.configuration;

import java.sql.Timestamp;

/**
 * Configuration DTO which contains general information name and description.
 *
 * @param name        configuration name
 * @param description configuration description
 */
public record ConfigurationGeneralDto(
        int id,
        String name,
        String description,
        Timestamp timestamp) {

  /**
   * Create DTO from Configuration entity.
   *
   * @param configuration configuration entity
   * @return the DTO object
   */
  public static ConfigurationGeneralDto from(Configuration configuration) {
    return new ConfigurationGeneralDto(
            configuration.getId(),
            configuration.getName(),
            configuration.getDescription(),
            configuration.getLastEdit()
    );
  }
}
