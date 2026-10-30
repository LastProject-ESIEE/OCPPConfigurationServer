package fr.uge.ocpp.domain.models;

public class TypeAllowedFactory {

  public static TypeAllowed dummyTypeAllowed() {
    return new TypeAllowed(1L, "Const", "Type");
  }

  public static TypeAllowed dummyTypeAllowedWithoutId() {
    return new TypeAllowed(null, "Const", "Type");
  }
}
