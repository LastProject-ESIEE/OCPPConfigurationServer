package fr.uge.ocpp.domain.models;

public class TypeAllowedFactory {

  public static TypeAllowed dummyTypeAllowed() {
    return new TypeAllowed(1L, "Const", "Type");
  }

}