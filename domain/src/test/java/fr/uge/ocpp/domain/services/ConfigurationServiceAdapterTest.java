package fr.uge.ocpp.domain.services;

import static fr.uge.ocpp.domain.models.ConfigurationFactory.dummyConfiguration;
import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.uge.ocpp.domain.exceptions.ConfigurationNotFoundException;
import fr.uge.ocpp.domain.exceptions.FirmwareNotFoundException;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.Sort;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepositoryStub;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepositoryStub;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ConfigurationServiceAdapterTest {

  private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 15, 10, 0);

  private final ConfigurationRepositoryStub configurationRepository =
      new ConfigurationRepositoryStub();
  private final FirmwareRepositoryStub firmwareRepository = new FirmwareRepositoryStub();
  private final ConfigurationServiceAdapter adapter =
      new ConfigurationServiceAdapter(
          configurationRepository,
          firmwareRepository,
          Clock.fixed(FIXED_TIME.toInstant(ZoneOffset.UTC), ZoneOffset.UTC));

  @AfterEach
  void tearDown() {
    configurationRepository.clear();
    firmwareRepository.clear();
  }

  @Test
  void should_search_configurations_by_request() {
    configurationRepository.add(dummyConfiguration().withId(1L).withName("alpha"));
    configurationRepository.add(dummyConfiguration().withId(2L).withName("beta"));

    var result = adapter.search("ALP", new PageQuery(0, 10, Sort.DESC));

    assertThat(result.content()).extracting(Configuration::name).containsExactly("alpha");
    assertThat(result.totalElements()).isEqualTo(1);
    assertThat(adapter.countTotalWithFilter("alp")).isEqualTo(1);
  }

  @Test
  void should_find_all_paginated() {
    configurationRepository.add(dummyConfiguration().withId(1L));
    configurationRepository.add(dummyConfiguration().withId(2L));

    var result = adapter.findAll(new PageQuery(0, 1, Sort.DESC));

    assertThat(result.content()).extracting(Configuration::id).containsExactly(2L);
    assertThat(result.totalElements()).isEqualTo(2);
  }

  @Test
  void should_not_save_when_firmware_not_found() {
    // given
    Firmware firmware = dummyFirmware().withId(5L);
    Configuration configuration = dummyConfiguration().withFirmware(firmware);

    // when + then
    assertThatThrownBy(() -> adapter.save(configuration))
        .isInstanceOf(FirmwareNotFoundException.class);
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
    assertThat(configurationRepository.configurations())
        .singleElement()
        .returns(configuration.name(), Configuration::name);
  }

  @Test
  void should_not_update_when_configuration_not_found() {
    // given
    Configuration configuration = dummyConfiguration().withId(1L);

    // when + then
    assertThatThrownBy(() -> adapter.update(1L, configuration))
        .isInstanceOf(ConfigurationNotFoundException.class);
  }

  @Test
  void should_update() {
    // given
    Configuration configuration = dummyConfiguration().withId(1L).withName("TEST");
    configurationRepository.add(configuration);

    // when
    Configuration actual = adapter.update(1L, configuration.withName("OTHER_NAME"));

    // then
    assertThat(actual)
        .returns("OTHER_NAME", Configuration::name)
        .returns(FIXED_TIME, Configuration::lastEdit);
  }
}
