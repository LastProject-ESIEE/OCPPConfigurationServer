package fr.uge.ocpp.database.factories;

import static fr.uge.ocpp.database.factories.TypeAllowedEntityFactory.dummyTypeAllowedEntity;

import fr.uge.ocpp.database.entities.FirmwareEntity;
import java.util.Set;

/** Factory of dummy {@link FirmwareEntity} for tests. */
public class FirmwareEntityFactory {

  public static FirmwareEntity dummyFirmwareEntity() {
    return new FirmwareEntity(1L, "url", "1.0.0", "Const", Set.of(dummyTypeAllowedEntity()));
  }

  public static FirmwareEntity dummyFirmwareEntityWithoutTypesAllowed() {
    return new FirmwareEntity(1L, "url", "1.0.0", "Const", Set.of());
  }
}
