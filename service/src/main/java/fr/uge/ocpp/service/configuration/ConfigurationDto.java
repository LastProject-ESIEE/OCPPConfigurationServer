package fr.uge.ocpp.service.configuration;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.service.firmware.FirmwareDto;
import java.sql.Timestamp;

/** A configuration exposed by the API. */
public record ConfigurationDto(long id,
                               String name,
                               String description,
                               Timestamp lastEdit,
                               String configuration,
                               FirmwareDto firmware) {

  /**
   * Maps a configuration domain model to the corresponding DTO.
   *
   * @param configuration the domain model to map.
   * @return the corresponding DTO.
   */
  public static ConfigurationDto of(Configuration configuration) {
    Timestamp lastEdit = configuration.lastEdit() == null ? null
        : Timestamp.valueOf(configuration.lastEdit());
    return new ConfigurationDto(configuration.id(), configuration.name(),
        configuration.description(), lastEdit, configuration.configuration(),
        configuration.firmware() == null ? null : FirmwareDto.of(configuration.firmware()));
  }
}
