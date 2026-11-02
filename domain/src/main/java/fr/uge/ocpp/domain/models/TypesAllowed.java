package fr.uge.ocpp.domain.models;

import java.util.Objects;
import java.util.Set;

/** A set of allowed types. */
public record TypesAllowed(Set<TypeAllowed> typesAllowedSet) {

  /** Creates the set, defaulting to an empty set. */
  public TypesAllowed {
    typesAllowedSet = Objects.requireNonNullElseGet(typesAllowedSet, Set::of);
  }
}
