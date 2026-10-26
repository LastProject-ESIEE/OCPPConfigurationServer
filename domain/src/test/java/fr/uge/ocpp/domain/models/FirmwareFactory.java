package fr.uge.ocpp.domain.models;

import static fr.uge.ocpp.domain.models.TypeAllowedFactory.dummyTypeAllowed;

import java.util.Set;

public class FirmwareFactory {

  public static Firmware dummyFirmware() {
    return new Firmware(1L, "url", "1.0.0", "Const", new TypesAllowed(Set.of(dummyTypeAllowed())));
  }
}
