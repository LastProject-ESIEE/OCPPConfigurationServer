package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepositoryStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static fr.uge.ocpp.domain.models.TypeAllowedFactory.dummyTypeAllowed;
import static fr.uge.ocpp.domain.models.TypeAllowedFactory.dummyTypeAllowedWithoutId;
import static org.assertj.core.api.Assertions.assertThat;

class TypeAllowedServiceAdapterTest {

  private final TypeAllowedRepositoryStub typeAllowedRepository = new TypeAllowedRepositoryStub();
  private final TypeAllowedServiceAdapter adapter =
      new TypeAllowedServiceAdapter(typeAllowedRepository);

  @AfterEach
  void tearDown() {
    typeAllowedRepository.clear();
  }

  @Test
  void should_find_all() {
    typeAllowedRepository.add(dummyTypeAllowed());

    assertThat(adapter.findAll().typesAllowedSet()).singleElement()
      .returns("Const", TypeAllowed::constructor);
  }

  @Test
  void should_save() {
    adapter.save(dummyTypeAllowedWithoutId());

    assertThat(typeAllowedRepository.typesAllowed()).singleElement()
      .returns("Type", TypeAllowed::type);
  }
}
