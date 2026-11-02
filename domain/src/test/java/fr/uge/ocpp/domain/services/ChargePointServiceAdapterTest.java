package fr.uge.ocpp.domain.services;

import static fr.uge.ocpp.domain.models.ChargePointFactory.dummyChargePoint;
import static fr.uge.ocpp.domain.models.ChargePointFactory.dummyChargePointWithoutConfiguration;
import static fr.uge.ocpp.domain.models.ConfigurationFactory.dummyConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.uge.ocpp.domain.exceptions.BadRequestException;
import fr.uge.ocpp.domain.exceptions.ChargePointAlreadyExistingException;
import fr.uge.ocpp.domain.exceptions.ChargePointNotFoundException;
import fr.uge.ocpp.domain.exceptions.ConfigurationNotFoundException;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepositoryStub;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepositoryStub;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ChargePointServiceAdapterTest {

  private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 15, 10, 0);

  private final ChargePointRepositoryStub chargePointRepository = new ChargePointRepositoryStub();
  private final ConfigurationRepositoryStub configurationRepository =
      new ConfigurationRepositoryStub();
  private final ChargePointServiceAdapter adapter =
      new ChargePointServiceAdapter(
          chargePointRepository,
          configurationRepository,
          Clock.fixed(FIXED_TIME.toInstant(ZoneOffset.UTC), ZoneOffset.UTC));

  @AfterEach
  void tearDown() {
    chargePointRepository.clear();
    configurationRepository.clear();
  }

  @Test
  void should_not_save_when_charge_point_already_existing() {
    chargePointRepository.add(dummyChargePoint().withId(3L));

    assertThatThrownBy(() -> adapter.save(dummyChargePoint()))
        .isInstanceOf(ChargePointAlreadyExistingException.class);
  }

  @Test
  void should_not_save_when_configuration_not_found() {
    ChargePoint chargePoint = dummyChargePoint().withId(null);

    assertThatThrownBy(() -> adapter.save(chargePoint))
        .isInstanceOf(ConfigurationNotFoundException.class);
  }

  @Test
  void should_not_save_when_fields_missing() {
    ChargePoint chargePoint = dummyChargePoint().withId(null).withSerialNumber(" ");

    assertThatThrownBy(() -> adapter.save(chargePoint)).isInstanceOf(BadRequestException.class);
  }

  @Test
  void should_save() {
    configurationRepository.add(dummyConfiguration());

    adapter.save(dummyChargePoint().withId(null));

    assertThat(chargePointRepository.chargePoints())
        .singleElement()
        .returns("client-id", ChargePoint::clientId);
  }

  @Test
  void should_not_find_by_id_when_charge_point_not_found() {
    assertThatThrownBy(() -> adapter.findById(42L))
        .isInstanceOf(ChargePointNotFoundException.class);
  }

  @Test
  void should_not_update_when_charge_point_not_found() {
    assertThatThrownBy(() -> adapter.update(42L, dummyChargePoint()))
        .isInstanceOf(ChargePointNotFoundException.class);
  }

  @Test
  void should_not_update_when_serial_number_and_constructor_already_used() {
    chargePointRepository.add(dummyChargePoint().withId(1L));
    chargePointRepository.add(dummyChargePoint().withId(2L).withSerialNumber("other"));

    assertThatThrownBy(() -> adapter.update(1L, dummyChargePoint().withSerialNumber("other")))
        .isInstanceOf(ChargePointAlreadyExistingException.class);
  }

  @Test
  void should_update_when_serial_number_and_constructor_kept() {
    chargePointRepository.add(dummyChargePoint().withId(1L));
    configurationRepository.add(dummyConfiguration());

    adapter.update(1L, dummyChargePoint().withType("NEW_TYPE"));

    assertThat(chargePointRepository.chargePoints())
        .singleElement()
        .returns("NEW_TYPE", ChargePoint::type)
        .returns(FIXED_TIME, ChargePoint::lastUpdate);
  }

  @Test
  void should_update_without_configuration() {
    chargePointRepository.add(dummyChargePoint().withId(1L));
    ChargePoint newValues = dummyChargePointWithoutConfiguration().withClientId("new-client");

    adapter.update(1L, newValues);

    assertThat(chargePointRepository.chargePoints())
        .singleElement()
        .returns(null, ChargePoint::configuration)
        .returns("new-client", ChargePoint::clientId);
  }

  @Test
  void should_not_update_when_configuration_not_found() {
    chargePointRepository.add(dummyChargePoint().withId(1L));
    Configuration unknownConfiguration = dummyConfiguration().withId(99L);
    ChargePoint newValues = dummyChargePoint().withConfiguration(unknownConfiguration);

    assertThatThrownBy(() -> adapter.update(1L, newValues))
        .isInstanceOf(ConfigurationNotFoundException.class);
  }
}
