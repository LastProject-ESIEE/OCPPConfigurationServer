package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.exceptions.BadRequestException;
import fr.uge.ocpp.domain.exceptions.FirmwareAlreadyExistingException;
import fr.uge.ocpp.domain.exceptions.FirmwareNotFoundException;
import fr.uge.ocpp.domain.exceptions.TypeAllowedNotFoundException;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepositoryStub;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepositoryStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;
import static fr.uge.ocpp.domain.models.TypeAllowedFactory.dummyTypeAllowed;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FirmwareServiceAdapterTest {

  private final FirmwareRepositoryStub firmwareRepository = new FirmwareRepositoryStub();
  private final TypeAllowedRepositoryStub typeAllowedRepository = new TypeAllowedRepositoryStub();
  private final FirmwareServiceAdapter adapter =
      new FirmwareServiceAdapter(firmwareRepository, typeAllowedRepository);

  @AfterEach
  void tearDown() {
    firmwareRepository.clear();
    typeAllowedRepository.clear();
  }

  @Test
  void should_not_save_when_url_already_used() {
    firmwareRepository.add(dummyFirmware());

    assertThatThrownBy(() -> adapter.save(dummyFirmware().withId(null)))
      .isInstanceOf(FirmwareAlreadyExistingException.class);
  }

  @Test
  void should_not_save_when_types_allowed_empty() {
    Firmware firmware = dummyFirmware().withId(null)
      .withTypesAllowed(new fr.uge.ocpp.domain.models.TypesAllowed(java.util.Set.of()));

    assertThatThrownBy(() -> adapter.save(firmware))
      .isInstanceOf(BadRequestException.class);
  }

  @Test
  void should_not_save_when_type_allowed_not_found() {
    assertThatThrownBy(() -> adapter.save(dummyFirmware().withId(null)))
      .isInstanceOf(TypeAllowedNotFoundException.class);
  }

  @Test
  void should_save() {
    typeAllowedRepository.add(dummyTypeAllowed());

    adapter.save(dummyFirmware().withId(null));

    assertThat(firmwareRepository.firmwares()).singleElement()
      .returns("1.0.0", Firmware::version);
  }

  @Test
  void should_not_find_by_id_when_firmware_not_found() {
    assertThatThrownBy(() -> adapter.findById(42L))
      .isInstanceOf(FirmwareNotFoundException.class);
  }

  @Test
  void should_not_update_when_firmware_not_found() {
    typeAllowedRepository.add(dummyTypeAllowed());

    assertThatThrownBy(() -> adapter.update(42L, dummyFirmware()))
      .isInstanceOf(FirmwareNotFoundException.class);
  }

  @Test
  void should_not_update_when_url_already_used_by_another_firmware() {
    firmwareRepository.add(dummyFirmware().withId(1L));
    firmwareRepository.add(dummyFirmware().withId(2L).withUrl("other"));
    typeAllowedRepository.add(dummyTypeAllowed());

    assertThatThrownBy(() -> adapter.update(1L, dummyFirmware().withUrl("other")))
      .isInstanceOf(FirmwareAlreadyExistingException.class);
  }

  @Test
  void should_update() {
    firmwareRepository.add(dummyFirmware());
    typeAllowedRepository.add(dummyTypeAllowed());

    adapter.update(1L, dummyFirmware().withVersion("2.0.0"));

    assertThat(firmwareRepository.firmwares()).singleElement()
      .returns("2.0.0", Firmware::version);
  }
}
