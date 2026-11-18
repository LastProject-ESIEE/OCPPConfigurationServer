package fr.uge.ocpp.database.factories;

import fr.uge.ocpp.domain.models.TypeAllowed;

/** Factory of dummy {@link TypeAllowed} domain models for tests. */
public class TypeAllowedFactory {

  public static TypeAllowed dummyTypeAllowed() {
    return new TypeAllowed(1L, "Const", "Type");
  }

  public static TypeAllowed dummyTypeAllowedWithoutId() {
    return new TypeAllowed(null, "Const", "Type");
  }
}
