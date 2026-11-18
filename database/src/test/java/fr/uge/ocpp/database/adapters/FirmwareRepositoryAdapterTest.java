package fr.uge.ocpp.database.adapters;

import static fr.uge.ocpp.database.DatabaseFixtures.persistFirmware;
import static fr.uge.ocpp.database.DatabaseFixtures.persistTypeAllowed;
import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;
import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.database.dao.FirmwareDao;
import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.database.entities.FirmwareEntity;
import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
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

/** Integration test of {@link FirmwareRepositoryAdapter} against a real (H2) database. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FirmwareRepositoryAdapterTest {

  @Autowired private FirmwareDao firmwareDao;
  @Autowired private TypeAllowedDao typeAllowedDao;

  private FirmwareRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new FirmwareRepositoryAdapter(firmwareDao, typeAllowedDao);
  }

  @Test
  void should_find_by_version_and_map_entity_to_domain_model() {
    // Given a persisted firmware with an allowed type
    var typeAllowed = persistTypeAllowed(typeAllowedDao);
    var firmware = persistFirmware(firmwareDao, typeAllowed);

    // When the firmware is looked up by version
    Optional<Firmware> found = adapter.findByVersion("1.0.0");

    // Then the persisted entity is mapped to a domain model, including its allowed types
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(firmware.getId());
    assertThat(found.get().url()).isEqualTo("url");
    assertThat(found.get().typesAllowed().typesAllowedSet())
        .extracting(TypeAllowed::id)
        .containsExactly(typeAllowed.getId());
  }

  @Test
  void should_return_empty_when_firmware_not_found_by_version() {
    // Given an empty database
    // When an unknown version is looked up
    Optional<Firmware> found = adapter.findByVersion("unknown");

    // Then the result is empty
    assertThat(found).isEmpty();
  }

  @Test
  void should_find_by_url_and_map_entity_to_domain_model() {
    // Given a persisted firmware
    persistFirmware(firmwareDao, persistTypeAllowed(typeAllowedDao));

    // When the firmware is looked up by url
    Optional<Firmware> found = adapter.findByUrl("url");

    // Then the persisted entity is mapped to a domain model
    assertThat(found).isPresent();
    assertThat(found.get().version()).isEqualTo("1.0.0");
  }

  @Test
  void should_return_empty_when_firmware_not_found_by_url() {
    // Given an empty database
    // When an unknown url is looked up
    Optional<Firmware> found = adapter.findByUrl("unknown");

    // Then the result is empty
    assertThat(found).isEmpty();
  }

  @Test
  void should_find_by_id_and_map_entity_to_domain_model() {
    // Given a persisted firmware
    var firmware = persistFirmware(firmwareDao, persistTypeAllowed(typeAllowedDao));

    // When the firmware is looked up by id
    Optional<Firmware> found = adapter.findById(firmware.getId());

    // Then the persisted entity is mapped to a domain model
    assertThat(found).isPresent();
    assertThat(found.get().id()).isEqualTo(firmware.getId());
    assertThat(found.get().constructor()).isEqualTo("Const");
  }

  @Test
  void should_return_empty_when_firmware_not_found_by_id() {
    // Given an empty database
    // When an unknown id is looked up
    Optional<Firmware> found = adapter.findById(999L);

    // Then the result is empty
    assertThat(found).isEmpty();
  }

  @Test
  void should_find_all_firmwares_ordered_by_id_desc() {
    // Given two persisted firmwares
    var typeAllowed = persistTypeAllowed(typeAllowedDao);
    var first = persistFirmware(firmwareDao, typeAllowed);
    var second = persistFirmware(firmwareDao, "2.0.0", typeAllowed);

    // When all the firmwares are requested
    Firmwares firmwares = adapter.findAllByOrderByIdDesc();

    // Then they are mapped to domain models ordered by descending id
    assertThat(firmwares.firmwareList())
        .extracting(Firmware::id)
        .containsExactly(second.getId(), first.getId());
  }

  @Test
  void should_find_paginated_firmwares_ordered_by_id_desc() {
    // Given three persisted firmwares
    var typeAllowed = persistTypeAllowed(typeAllowedDao);
    persistFirmware(firmwareDao, "1.0.0", typeAllowed);
    var second = persistFirmware(firmwareDao, "2.0.0", typeAllowed);
    var third = persistFirmware(firmwareDao, "3.0.0", typeAllowed);

    // When the first page of size 2 is requested
    PageResult<Firmware> result = adapter.findAllByOrderByIdDesc(new PageQuery(0, 2, Sort.DESC));

    // Then the two newest firmwares are returned with the total count
    assertThat(result.content())
        .extracting(Firmware::id)
        .containsExactly(third.getId(), second.getId());
    assertThat(result.totalElements()).isEqualTo(3);
    assertThat(result.page()).isZero();
    assertThat(result.size()).isEqualTo(2);
  }

  @Test
  void should_find_firmwares_by_type_allowed_asc() {
    // Given two firmwares compatible with the same allowed type
    var typeAllowed = persistTypeAllowed(typeAllowedDao);
    persistFirmware(firmwareDao, "2.0.0", typeAllowed);
    persistFirmware(firmwareDao, "1.0.0", typeAllowed);
    // And a firmware compatible with another type, which must be excluded
    persistFirmware(firmwareDao, "3.0.0", persistTypeAllowed(typeAllowedDao));

    // When the firmwares of the allowed type are requested ascending
    Firmwares firmwares = adapter.findAllByTypeAllowedAsc(toDomain(typeAllowed));

    // Then only the compatible firmwares are returned, ordered by ascending version
    assertThat(firmwares.firmwareList())
        .extracting(Firmware::version)
        .containsExactly("1.0.0", "2.0.0");
  }

  @Test
  void should_find_firmwares_by_type_allowed_desc() {
    // Given two firmwares compatible with the same allowed type
    var typeAllowed = persistTypeAllowed(typeAllowedDao);
    persistFirmware(firmwareDao, "1.0.0", typeAllowed);
    persistFirmware(firmwareDao, "2.0.0", typeAllowed);

    // When the firmwares of the allowed type are requested descending
    Firmwares firmwares = adapter.findAllByTypeAllowedDesc(toDomain(typeAllowed));

    // Then only the compatible firmwares are returned, ordered by descending version
    assertThat(firmwares.firmwareList())
        .extracting(Firmware::version)
        .containsExactly("2.0.0", "1.0.0");
  }

  @Test
  void should_return_empty_when_type_allowed_unknown_asc() {
    // Given a persisted firmware
    persistFirmware(firmwareDao, persistTypeAllowed(typeAllowedDao));

    // When the firmwares of an unknown allowed type are requested
    Firmwares firmwares = adapter.findAllByTypeAllowedAsc(new TypeAllowed(999L, "x", "y"));

    // Then the result is empty (the adapter falls back to an id-only reference)
    assertThat(firmwares.firmwareList()).isEmpty();
  }

  @Test
  void should_return_empty_when_type_allowed_unknown_desc() {
    // Given a persisted firmware
    persistFirmware(firmwareDao, persistTypeAllowed(typeAllowedDao));

    // When the firmwares of an unknown allowed type are requested
    Firmwares firmwares = adapter.findAllByTypeAllowedDesc(new TypeAllowed(999L, "x", "y"));

    // Then the result is empty (the adapter falls back to an id-only reference)
    assertThat(firmwares.firmwareList()).isEmpty();
  }

  @Test
  void should_save_and_map_domain_model_to_entity() {
    // Given an empty database
    // When a new domain firmware is saved
    Firmware saved =
        adapter.save(dummyFirmware().withId(null).withTypesAllowed(TypesAllowed.empty()));

    // Then the firmware is persisted with a generated id and mapped back to the domain
    assertThat(saved.id()).isNotNull();
    assertThat(saved.version()).isEqualTo("1.0.0");
    Optional<FirmwareEntity> persisted = firmwareDao.findById(saved.id());
    assertThat(persisted).isPresent();
    assertThat(persisted.get().getUrl()).isEqualTo("url");
    assertThat(persisted.get().getTypesAllowed()).isEmpty();
  }

  private static TypeAllowed toDomain(TypeAllowedEntity entity) {
    return new TypeAllowed(entity.getId(), entity.getConstructor(), entity.getType());
  }
}
