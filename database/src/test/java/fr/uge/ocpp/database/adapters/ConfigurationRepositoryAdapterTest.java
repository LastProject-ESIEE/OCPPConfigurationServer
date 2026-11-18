package fr.uge.ocpp.database.adapters;

import static fr.uge.ocpp.database.DatabaseFixtures.persistConfiguration;
import static fr.uge.ocpp.database.DatabaseFixtures.persistFirmware;
import static fr.uge.ocpp.database.DatabaseFixtures.persistTypeAllowed;
import static fr.uge.ocpp.database.factories.ConfigurationFactory.dummyConfiguration;
import static fr.uge.ocpp.database.factories.FirmwareFactory.dummyFirmware;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.database.dao.ConfigurationDao;
import fr.uge.ocpp.database.dao.FirmwareDao;
import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.database.entities.ConfigurationEntity;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.Sort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** Integration test of {@link ConfigurationRepositoryAdapter} against a real (H2) database. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConfigurationRepositoryAdapterTest {

  @Autowired private ConfigurationDao configurationDao;
  @Autowired private FirmwareDao firmwareDao;
  @Autowired private TypeAllowedDao typeAllowedDao;

  private ConfigurationRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new ConfigurationRepositoryAdapter(configurationDao);
  }

  @Test
  void should_find_all_configurations_ordered_by_id_desc() {
    // Given two persisted configurations
    var first = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var second = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);

    // When all the configurations are requested
    Configurations configurations = adapter.findAllByOrderByIdDesc();

    // Then they are mapped to domain models ordered by descending id
    assertThat(configurations.configurationList())
        .extracting(Configuration::id)
        .containsExactly(second.getId(), first.getId());
  }

  @Test
  void should_find_paginated_configurations_ordered_by_id_desc() {
    // Given three persisted configurations
    var first = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var second = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var third = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);

    // When the first page of size 2 is requested
    PageResult<Configuration> result =
        adapter.findAllByOrderByIdDesc(new PageQuery(0, 2, Sort.DESC));

    // Then the two newest configurations are returned with the total count
    assertThat(result.content())
        .extracting(Configuration::id)
        .containsExactly(third.getId(), second.getId());
    assertThat(result.totalElements()).isEqualTo(3);
    assertThat(result.page()).isZero();
    assertThat(result.size()).isEqualTo(2);

    // When the second page of size 2 is requested
    PageResult<Configuration> secondPage =
        adapter.findAllByOrderByIdDesc(new PageQuery(1, 2, Sort.DESC));

    // Then only the oldest configuration remains
    assertThat(secondPage.content()).extracting(Configuration::id).containsExactly(first.getId());
    assertThat(secondPage.totalElements()).isEqualTo(3);
  }

  @Test
  void should_save_and_map_domain_model_to_entity() {
    // Given a persisted firmware
    var firmware = persistFirmware(firmwareDao, persistTypeAllowed(typeAllowedDao));

    // When a new domain configuration referencing it is saved
    Configuration saved =
        adapter.save(
            dummyConfiguration()
                .withId(null)
                .withFirmware(dummyFirmware().withId(firmware.getId())));

    // Then the configuration is persisted with a generated id and mapped back to the domain
    assertThat(saved.id()).isNotNull();
    assertThat(saved.name()).isEqualTo("test");
    assertThat(saved.firmware().id()).isEqualTo(firmware.getId());
    Optional<ConfigurationEntity> persisted = configurationDao.findById(saved.id());
    assertThat(persisted).isPresent();
    assertThat(persisted.get().getName()).isEqualTo("test");
    assertThat(persisted.get().getFirmware().getId()).isEqualTo(firmware.getId());
  }

  @Test
  void should_find_by_id_and_map_entity_to_domain_model() {
    // Given a persisted configuration
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);

    // When the configuration is looked up by id
    Optional<Configuration> found = adapter.findById(configuration.getId());

    // Then the persisted entity is mapped to a domain model, including its firmware
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(configuration.getId());
    assertThat(found.get().name()).isEqualTo("test");
    assertThat(found.get().firmware().version()).isEqualTo("1.0.0");
  }

  @Test
  void should_return_empty_when_configuration_not_found_by_id() {
    // Given an empty database
    // When an unknown id is looked up
    Optional<Configuration> found = adapter.findById(999L);

    // Then the result is empty
    assertThat(found).isEmpty();
  }
}
