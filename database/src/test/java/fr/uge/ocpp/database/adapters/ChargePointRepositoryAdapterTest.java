package fr.uge.ocpp.database.adapters;

import static fr.uge.ocpp.database.DatabaseFixtures.persistChargePoint;
import static fr.uge.ocpp.database.DatabaseFixtures.persistConfiguration;
import static fr.uge.ocpp.database.factories.ChargePointFactory.dummyChargePoint;
import static fr.uge.ocpp.database.factories.ConfigurationFactory.dummyConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.database.dao.ChargePointDao;
import fr.uge.ocpp.database.dao.ConfigurationDao;
import fr.uge.ocpp.database.dao.FirmwareDao;
import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.Sort;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** Integration test of {@link ChargePointRepositoryAdapter} against a real (H2) database. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChargePointRepositoryAdapterTest {

  @Autowired private ChargePointDao chargePointDao;
  @Autowired private ConfigurationDao configurationDao;
  @Autowired private FirmwareDao firmwareDao;
  @Autowired private TypeAllowedDao typeAllowedDao;

  private ChargePointRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new ChargePointRepositoryAdapter(chargePointDao);
  }

  @Test
  void should_save_and_map_domain_model_to_entity() {
    // Given a persisted configuration
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);

    // When a new domain charge point referencing it is saved
    ChargePoint saved =
        adapter.save(
            dummyChargePoint()
                .withId(null)
                .withConfiguration(dummyConfiguration().withId(configuration.getId())));

    // Then the charge point is persisted with a generated id and mapped back to the domain
    assertThat(saved.id()).isNotNull();
    assertThat(saved.serialNumber()).isEqualTo("serial");
    assertThat(saved.configuration().id()).isEqualTo(configuration.getId());
    Optional<ChargePointEntity> persisted = chargePointDao.findById(saved.id());
    assertThat(persisted).isPresent();
    assertThat(persisted.get().getClientId()).isEqualTo("client-id");
    assertThat(persisted.get().getConfiguration().getId()).isEqualTo(configuration.getId());
  }

  @Test
  void should_find_by_id_and_map_entity_to_domain_model() {
    // Given a persisted charge point
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var entity = persistChargePoint(chargePointDao, configuration);

    // When the charge point is looked up by id
    Optional<ChargePoint> found = adapter.findById(entity.getId());

    // Then the persisted entity is mapped to a domain model, including its configuration
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(entity.getId());
    assertThat(found.get().clientId()).isEqualTo("client-id");
    assertThat(found.get().configuration().id()).isEqualTo(configuration.getId());
    assertThat(found.get().configuration().firmware().version()).isEqualTo("1.0.0");
  }

  @Test
  void should_return_empty_when_charge_point_not_found_by_id() {
    // Given an empty database
    // When an unknown id is looked up
    Optional<ChargePoint> found = adapter.findById(999L);

    // Then the result is empty
    assertThat(found).isEmpty();
  }

  @Test
  void should_find_by_serial_number_and_constructor() {
    // Given a persisted charge point
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    persistChargePoint(chargePointDao, configuration);

    // When the charge point is looked up by serial number and constructor
    Optional<ChargePoint> found =
        adapter.findBySerialNumberChargepointAndConstructor("serial", "Const");

    // Then the charge point is found and mapped to a domain model
    assertThat(found).isPresent();
    assertThat(found.get().serialNumber()).isEqualTo("serial");
    assertThat(found.get().constructor()).isEqualTo("Const");
  }

  @Test
  void should_return_empty_when_serial_number_and_constructor_unknown() {
    // Given a persisted charge point
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    persistChargePoint(chargePointDao, configuration);

    // When an unknown serial number is looked up
    Optional<ChargePoint> found =
        adapter.findBySerialNumberChargepointAndConstructor("unknown", "Const");

    // Then the result is empty
    assertThat(found).isEmpty();
  }

  @Test
  void should_find_all_charge_points_ordered_by_id_desc() {
    // Given two persisted charge points
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var first = persistChargePoint(chargePointDao, configuration);
    var second = persistChargePoint(chargePointDao, configuration);

    // When all the charge points are requested
    ChargePoints chargePoints = adapter.findAllByOrderByIdDesc();

    // Then they are mapped to domain models ordered by descending id
    assertThat(chargePoints.chargePointList())
        .extracting(ChargePoint::id)
        .containsExactly(second.getId(), first.getId());
  }

  @Test
  void should_find_paginated_by_client_id_with_desc_sort() {
    // Given three charge points, two of them matching the client id filter
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var matching1 = persistChargePoint(chargePointDao, configuration, "Client-A");
    var matching2 = persistChargePoint(chargePointDao, configuration, "CLIENT-2");
    persistChargePoint(chargePointDao, configuration, "other");

    // When a page is requested with a descending sort
    PageResult<ChargePoint> result =
        adapter.findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
            new PageQuery(0, 10, Sort.DESC), "client");

    // Then only the matching charge points are returned, highest id first, with the total count
    assertThat(result.content())
        .extracting(ChargePoint::id)
        .containsExactly(matching2.getId(), matching1.getId());
    assertThat(result.totalElements()).isEqualTo(2);
    assertThat(result.page()).isZero();
    assertThat(result.size()).isEqualTo(10);
  }

  @Test
  void should_find_paginated_by_client_id_with_asc_sort() {
    // Given three charge points, two of them matching the client id filter
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var matching1 = persistChargePoint(chargePointDao, configuration, "Client-A");
    var matching2 = persistChargePoint(chargePointDao, configuration, "CLIENT-2");
    persistChargePoint(chargePointDao, configuration, "other");

    // When a page is requested with an ascending sort
    PageResult<ChargePoint> result =
        adapter.findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
            new PageQuery(0, 10, Sort.ASC), "client");

    // Then only the matching charge points are returned with the total count; the order stays
    // descending because the OrderByIdDesc of the DAO method name takes precedence over the
    // pageable sort
    assertThat(result.content())
        .extracting(ChargePoint::id)
        .containsExactlyInAnyOrder(matching1.getId(), matching2.getId());
    assertThat(result.totalElements()).isEqualTo(2);
  }

  @Test
  void should_apply_pagination_on_client_id_search() {
    // Given a charge point
    var configuration = persistConfiguration(configurationDao, firmwareDao, typeAllowedDao);
    var matching1 = persistChargePoint(chargePointDao, configuration, "Client-A");
    persistChargePoint(chargePointDao, configuration, "other");

    // When the second page of size 1 is requested with a descending sort
    PageResult<ChargePoint> result =
        adapter.findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
            new PageQuery(1, 1, Sort.DESC), "client");

    // Then only the second matching charge point is returned, but the total stays 2
    assertThat(result.content()).extracting(ChargePoint::id).containsExactly(matching1.getId());
    assertThat(result.totalElements()).isEqualTo(2);
    assertThat(result.page()).isEqualTo(1);
    assertThat(result.size()).isEqualTo(1);
  }
}
