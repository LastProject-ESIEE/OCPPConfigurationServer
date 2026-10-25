package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.exceptions.FirmwareNotFoundException;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepositoryStub;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepositoryStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static fr.uge.ocpp.domain.models.ConfigurationFactory.dummyConfiguration;
import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfigurationServiceAdapterTest {

  private final ConfigurationRepositoryStub configurationRepository = new ConfigurationRepositoryStub();
  private final FirmwareRepositoryStub firmwareRepository = new FirmwareRepositoryStub();
  private final ConfigurationServiceAdapter adapter = new ConfigurationServiceAdapter(configurationRepository, firmwareRepository);

  @AfterEach
  void tearDown() {
    configurationRepository.clear();
    firmwareRepository.clear();
  }

  @Test
  void should_not_save_when_firmware_not_found() {
    // given
    Firmware firmware = dummyFirmware().withId(5L);
    Configuration configuration = dummyConfiguration().withFirmware(firmware);

    // when + then
    assertThatThrownBy(() -> adapter.save(configuration)).isInstanceOf(FirmwareNotFoundException.class);
  }

  @Test
  void should_save() {
    // given
    Firmware firmware = dummyFirmware().withId(5L);
    Configuration configuration = dummyConfiguration().withFirmware(firmware).withName("TEST");
    firmwareRepository.add(firmware);

    // when
    adapter.save(configuration);

    // then
    assertThat(configurationRepository.configurations()).singleElement()
      .returns(configuration.name(), Configuration::name);
  }



}