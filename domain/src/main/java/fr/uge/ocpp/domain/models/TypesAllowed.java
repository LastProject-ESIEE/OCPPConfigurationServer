package fr.uge.ocpp.domain.models;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/** A set of allowed types. */
public record TypesAllowed(Set<TypeAllowed> typesAllowedSet) {

  /** Creates the set, defaulting to an empty set. */
  public TypesAllowed {
    typesAllowedSet = Objects.requireNonNullElseGet(typesAllowedSet, Set::of);
  }

  public static TypesAllowed empty() {
    return new TypesAllowed(Set.of());
  }

  public Stream<TypeAllowed> stream() {
    return typesAllowedSet.stream();
  }
}
