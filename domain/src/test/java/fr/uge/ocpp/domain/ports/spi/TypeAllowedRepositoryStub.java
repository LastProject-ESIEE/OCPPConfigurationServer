package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;

import java.util.LinkedHashSet;
import java.util.Set;

public class TypeAllowedRepositoryStub implements TypeAllowedRepository {

  private final Set<TypeAllowed> typesAllowed = new LinkedHashSet<>();

  public void clear() {
    typesAllowed.clear();
  }

  public Set<TypeAllowed> typesAllowed() {
    return typesAllowed;
  }

  public void add(TypeAllowed typeAllowed) {
    typesAllowed.add(typeAllowed);
  }

  @Override
  public TypesAllowed findAll() {
    return new TypesAllowed(Set.copyOf(typesAllowed));
  }

  @Override
  public java.util.Optional<TypeAllowed> findById(long id) {
    return typesAllowed.stream()
      .filter(typeAllowed -> typeAllowed.id() != null && typeAllowed.id() == id)
      .findFirst();
  }

  @Override
  public TypeAllowed save(TypeAllowed typeAllowed) {
    typesAllowed.removeIf(existing -> typeAllowed.id() != null
      && typeAllowed.id().equals(existing.id()));
    typesAllowed.add(typeAllowed);

    return typeAllowed;
  }
}
