package fr.uge.ocpp.database.factories;

import static fr.uge.ocpp.database.factories.FirmwareEntityFactory.dummyFirmwareEntity;

import fr.uge.ocpp.database.entities.ConfigurationEntity;
import java.time.LocalDateTime;

/** Factory of dummy {@link ConfigurationEntity} for tests. */
public class ConfigurationEntityFactory {

  public static ConfigurationEntity dummyConfigurationEntity() {
    var entity =
        ConfigurationEntity.builder()
            .name("test")
            .description("desc")
            .configuration("{}")
            .firmware(dummyFirmwareEntity())
            .build();
    entity.setId(1L);
    entity.setLastEdit(LocalDateTime.now());
    return entity;
  }

  public static ConfigurationEntity dummyConfigurationEntityWithoutFirmware() {
    var entity =
        ConfigurationEntity.builder()
            .name("test")
            .description("desc")
            .configuration("{}")
            .firmware(null)
            .build();
    entity.setId(1L);
    entity.setLastEdit(LocalDateTime.now());
    return entity;
  }
}
