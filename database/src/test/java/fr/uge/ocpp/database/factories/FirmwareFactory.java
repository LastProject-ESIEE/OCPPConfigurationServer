package fr.uge.ocpp.database.factories;

import static fr.uge.ocpp.database.factories.TypeAllowedFactory.dummyTypeAllowed;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.TypesAllowed;
import java.util.Set;

/** Factory of dummy {@link Firmware} domain models for tests. */
public class FirmwareFactory {

  public static Firmware dummyFirmware() {
    return new Firmware(1L, "url", "1.0.0", "Const", new TypesAllowed(Set.of(dummyTypeAllowed())));
  }

  public static Firmware dummyFirmwareWithoutTypesAllowed() {
    return new Firmware(1L, "url", "1.0.0", "Const", null);
  }
}
