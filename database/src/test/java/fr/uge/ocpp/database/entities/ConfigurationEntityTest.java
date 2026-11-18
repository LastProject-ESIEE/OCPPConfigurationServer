package fr.uge.ocpp.database.entities;

import static fr.uge.ocpp.database.factories.ConfigurationEntityFactory.dummyConfigurationEntityWithoutFirmware;
import static fr.uge.ocpp.database.factories.ConfigurationFactory.dummyConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConfigurationEntityTest {

  @Test
  void should_map_domain_model_to_entity() {
    // Given a domain configuration with a firmware
    var configuration = dummyConfiguration();

    // When the configuration is mapped to an entity
    ConfigurationEntity entity = ConfigurationEntity.fromDomain(configuration);

    // Then every field is copied and the firmware is mapped to an entity
    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getName()).isEqualTo("test");
    assertThat(entity.getDescription()).isEqualTo("desc");
    assertThat(entity.getConfiguration()).isEqualTo("{}");
    assertThat(entity.getFirmware()).isNotNull();
    assertThat(entity.getFirmware().getId()).isEqualTo(1L);
  }

  @Test
  void should_map_entity_to_domain_model() {
    // Given a configuration entity with a firmware
    var entity =
        ConfigurationEntity.builder()
            .id(1L)
            .name("test")
            .description("desc")
            .configuration("{}")
            .firmware(new FirmwareEntity(1L, "url", "1.0.0", "Const", java.util.Set.of()))
            .build();

    // When the entity is mapped to a domain model
    var configuration = entity.toDomain();

    // Then every field is copied and the firmware is mapped to a domain model
    assertThat(configuration.id()).isEqualTo(1L);
    assertThat(configuration.name()).isEqualTo("test");
    assertThat(configuration.description()).isEqualTo("desc");
    assertThat(configuration.configuration()).isEqualTo("{}");
    assertThat(configuration.firmware()).isNotNull();
    assertThat(configuration.firmware().version()).isEqualTo("1.0.0");
  }

  @Test
  void should_map_entity_without_firmware_to_domain_model_with_null_firmware() {
    // Given a configuration entity without firmware
    var entity = dummyConfigurationEntityWithoutFirmware();

    // When the entity is mapped to a domain model
    var configuration = entity.toDomain();

    // Then the firmware is null
    assertThat(configuration.id()).isEqualTo(1L);
    assertThat(configuration.firmware()).isNull();
  }
}
