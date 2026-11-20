package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
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
  public PageResult<TypeAllowed> findAllByOrderByIdDesc(PageQuery pageQuery) {
    List<TypeAllowed> result =
        typesAllowed.stream()
            .sorted(Comparator.comparing(TypeAllowed::id).reversed())
            .skip((long) pageQuery.page() * pageQuery.size())
            .limit(pageQuery.size())
            .toList();

    return new PageResult<>(result, pageQuery.page(), pageQuery.size(), typesAllowed.size());
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
    typesAllowed.removeIf(
        existing -> typeAllowed.id() != null && typeAllowed.id().equals(existing.id()));
    typesAllowed.add(typeAllowed);

    return typeAllowed;
  }
}
