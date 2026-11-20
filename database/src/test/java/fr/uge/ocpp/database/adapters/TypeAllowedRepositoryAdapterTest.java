package fr.uge.ocpp.database.adapters;

import static fr.uge.ocpp.database.DatabaseFixtures.persistTypeAllowed;
import static fr.uge.ocpp.domain.models.TypeAllowedFactory.dummyTypeAllowedWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.Sort;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** Integration test of {@link TypeAllowedRepositoryAdapter} against a real (H2) database. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TypeAllowedRepositoryAdapterTest {

  @Autowired private TypeAllowedDao typeAllowedDao;

  private TypeAllowedRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new TypeAllowedRepositoryAdapter(typeAllowedDao);
  }

  @Test
  void should_find_paginated_allowed_types_ordered_by_id_desc() {
    // Given three persisted allowed types
    var first = persistTypeAllowed(typeAllowedDao);
    var second = persistTypeAllowed(typeAllowedDao);
    var third = persistTypeAllowed(typeAllowedDao);

    // When the first page of size 2 is requested, descending
    PageResult<TypeAllowed> result = adapter.findAllByOrderByIdDesc(new PageQuery(0, 2, Sort.DESC));

    // Then the two newest allowed types are returned with the total count
    assertThat(result.content())
        .extracting(TypeAllowed::id)
        .containsExactly(third.getId(), second.getId());
    assertThat(result.totalElements()).isEqualTo(3);
  }

  @Test
  void should_find_all_and_map_entities_to_domain_models() {
    // Given two persisted allowed types
    var first = persistTypeAllowed(typeAllowedDao);
    var second = persistTypeAllowed(typeAllowedDao);

    // When all the allowed types are requested
    TypesAllowed typesAllowed = adapter.findAll();

    // Then they are mapped to a set of domain models
    assertThat(typesAllowed.typesAllowedSet())
        .extracting(TypeAllowed::id)
        .containsExactlyInAnyOrder(first.getId(), second.getId());
    assertThat(typesAllowed.typesAllowedSet())
        .extracting(TypeAllowed::constructor)
        .containsOnly("Const");
  }

  @Test
  void should_find_by_id_and_map_entity_to_domain_model() {
    // Given a persisted allowed type
    var entity = persistTypeAllowed(typeAllowedDao);

    // When the allowed type is looked up by id
    Optional<TypeAllowed> found = adapter.findById(entity.getId());

    // Then the persisted entity is mapped to a domain model
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(entity.getId());
    assertThat(found.get().constructor()).isEqualTo("Const");
    assertThat(found.get().type()).isEqualTo("Type");
  }

  @Test
  void should_return_empty_when_type_allowed_not_found_by_id() {
    // Given an empty database
    // When an unknown id is looked up
    Optional<TypeAllowed> found = adapter.findById(999L);

    // Then the result is empty
    assertThat(found).isEmpty();
  }

  @Test
  void should_save_and_map_domain_model_to_entity() {
    // Given an empty database
    // When a new domain allowed type is saved
    TypeAllowed saved = adapter.save(dummyTypeAllowedWithoutId());

    // Then the allowed type is persisted with a generated id and mapped back to the domain
    assertThat(saved.id()).isNotNull();
    assertThat(saved.constructor()).isEqualTo("Const");
    Optional<TypeAllowed> persisted = adapter.findById(saved.id());
    assertThat(persisted).isPresent();
    assertThat(persisted.get().type()).isEqualTo("Type");
  }
}
