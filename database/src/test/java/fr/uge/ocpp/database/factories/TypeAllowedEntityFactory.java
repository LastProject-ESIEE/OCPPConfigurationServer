package fr.uge.ocpp.database.factories;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import java.util.Set;

/** Factory of dummy {@link TypeAllowedEntity} for tests. */
public class TypeAllowedEntityFactory {

  public static TypeAllowedEntity dummyTypeAllowedEntity() {
    var entity = new TypeAllowedEntity();
    entity.setId(1L);
    entity.setConstructor("Const");
    entity.setType("Type");
    entity.setFirmwares(Set.of());
    return entity;
  }
}
