package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;

import static fr.uge.ocpp.domain.models.FirmwareFactory.dummyFirmware;

public class ConfigurationFactory {

  public static Configuration dummyConfiguration() {
    return new Configuration(1L, "test", "test", LocalDateTime.now(), "config", dummyFirmware());
  }

}