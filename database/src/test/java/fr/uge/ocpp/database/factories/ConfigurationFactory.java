package fr.uge.ocpp.database.factories;

import static fr.uge.ocpp.database.factories.FirmwareFactory.dummyFirmware;

import fr.uge.ocpp.domain.models.Configuration;
import java.time.LocalDateTime;

/** Factory of dummy {@link Configuration} domain models for tests. */
public class ConfigurationFactory {

  public static Configuration dummyConfiguration() {
    return new Configuration(1L, "test", "desc", LocalDateTime.now(), "{}", dummyFirmware());
  }
}
