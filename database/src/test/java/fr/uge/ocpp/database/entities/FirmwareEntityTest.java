package fr.uge.ocpp.database.entities;

import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.domain.models.TypeAllowed;
import org.junit.jupiter.api.Test;

class FirmwareEntityTest {

  @Test
  void should_map_domain_model_to_entity() {
    // Given a domain firmware with one allowed type
    var firmware = dummyFirmware();

    // When the firmware is mapped to an entity
    FirmwareEntity entity = FirmwareEntity.fromDomain(firmware);

    // Then every field is copied and the allowed types are mapped to entities
    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getUrl()).isEqualTo("url");
    assertThat(entity.getVersion()).isEqualTo("1.0.0");
    assertThat(entity.getConstructor()).isEqualTo("Const");
    assertThat(entity.getTypesAllowed()).extracting(TypeAllowedEntity::getId).containsExactly(1L);
  }

  @Test
  void should_map_domain_model_with_null_types_allowed_to_entity_with_empty_set() {
    // Given a domain firmware without allowed types
    var firmware = dummyFirmware().withTypesAllowed(null);

    // When the firmware is mapped to an entity
    FirmwareEntity entity = FirmwareEntity.fromDomain(firmware);

    // Then the allowed types default to an empty set
    assertThat(entity.getTypesAllowed()).isNotNull().isEmpty();
  }

  @Test
  void should_map_entity_to_domain_model() {
    // Given a firmware entity with one allowed type
    var entity =
        new FirmwareEntity(
            1L,
            "url",
            "1.0.0",
            "Const",
            java.util.Set.of(new TypeAllowedEntity(1L, "Const", "Type", java.util.Set.of())));

    // When the entity is mapped to a domain model
    var firmware = entity.toDomain();

    // Then every field is copied and the allowed types are mapped to domain models
    assertThat(firmware.id()).isEqualTo(1L);
    assertThat(firmware.url()).isEqualTo("url");
    assertThat(firmware.version()).isEqualTo("1.0.0");
    assertThat(firmware.constructor()).isEqualTo("Const");
    assertThat(firmware.typesAllowed().typesAllowedSet())
        .extracting(TypeAllowed::id)
        .containsExactly(1L);
  }

  @Test
  void should_map_entity_without_types_allowed_to_domain_model_with_empty_set() {
    // Given a firmware entity without allowed types
    var entity = new FirmwareEntity(1L, "url", "1.0.0", "Const", java.util.Set.of());

    // When the entity is mapped to a domain model
    var firmware = entity.toDomain();

    // Then the allowed types are an empty set
    assertThat(firmware.typesAllowed().typesAllowedSet()).isEmpty();
  }
}
