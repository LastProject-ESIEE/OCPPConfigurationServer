package fr.uge.ocpp.service.configuration;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.TypesAllowed;

/** Maps the configuration DTOs to the domain models. */
final class ConfigurationMapper {

  /** Creates a new mapper, this class should not be instantiated. */
  private ConfigurationMapper() {
  }

  /**
   * Maps the creation values to a configuration domain model.
   *
   * @param dto the creation values.
   * @return the corresponding domain model.
   */
  static Configuration toConfiguration(CreateConfigurationDto dto) {
    var firmware = new Firmware(dto.firmware(), null, null, null, new TypesAllowed(null));
    return new Configuration(null, dto.name(), dto.description(), null, dto.configuration(),
        firmware);
  }
}
