package fr.uge.ocpp.database.entities;

import static fr.uge.ocpp.domain.models.TypeAllowedFactory.dummyTypeAllowed;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TypeAllowedEntityTest {

  @Test
  void should_create_reference_with_only_id() {
    // Given an identifier
    // When a reference entity is created
    TypeAllowedEntity reference = TypeAllowedEntity.reference(42L);

    // Then only the identifier is set
    assertThat(reference.getId()).isEqualTo(42L);
    assertThat(reference.getConstructor()).isNull();
    assertThat(reference.getType()).isNull();
  }

  @Test
  void should_map_domain_model_to_entity() {
    // Given a domain allowed type
    var typeAllowed = dummyTypeAllowed();

    // When the allowed type is mapped to an entity
    TypeAllowedEntity entity = TypeAllowedEntity.fromDomain(typeAllowed);

    // Then the fields are copied and the firmwares are empty
    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getConstructor()).isEqualTo("Const");
    assertThat(entity.getType()).isEqualTo("Type");
    assertThat(entity.getFirmwares()).isNotNull().isEmpty();
  }

  @Test
  void should_map_entity_to_domain_model() {
    // Given an allowed type entity
    var entity = new TypeAllowedEntity(1L, "Const", "Type", java.util.Set.of());

    // When the entity is mapped to a domain model
    var typeAllowed = entity.toDomain();

    // Then the fields are copied
    assertThat(typeAllowed.id()).isEqualTo(1L);
    assertThat(typeAllowed.constructor()).isEqualTo("Const");
    assertThat(typeAllowed.type()).isEqualTo("Type");
  }
}
