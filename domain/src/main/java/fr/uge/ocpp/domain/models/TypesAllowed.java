package fr.uge.ocpp.domain.models;

import java.util.Objects;
import java.util.Set;

public record TypesAllowed(Set<TypeAllowed> typesAllowedSet) {

  public TypesAllowed {
    typesAllowedSet = Objects.requireNonNullElseGet(typesAllowedSet, Set::of);
  }
}
