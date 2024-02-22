package fr.uge.chargepointconfiguration.entities.dto;

import fr.uge.chargepointconfiguration.entities.Configuration;

/**
 * Configuration DTO which contains general information name and description.
 *
 * @param name        configuration name
 * @param description configuration description
 */
public record ConfigurationGeneralDto(int id, String name, String description) {

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
            configuration.getDescription()
    );
  }
}
