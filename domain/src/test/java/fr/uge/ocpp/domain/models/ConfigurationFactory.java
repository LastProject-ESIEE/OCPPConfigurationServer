package fr.uge.ocpp.domain.models;

import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;

import java.time.LocalDateTime;

public class ConfigurationFactory {

  public static Configuration dummyConfiguration() {
    return new Configuration(1L, "test", "test", LocalDateTime.now(), "config", dummyFirmware());
  }
}
