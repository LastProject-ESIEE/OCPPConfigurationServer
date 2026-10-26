package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.database.repositories.TypeAllowedJpaRepository;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the allowed types in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
public class TypeAllowedRepositoryAdapter implements TypeAllowedRepository {

  private final TypeAllowedJpaRepository typeAllowedJpaRepository;

  /**
   * Creates the adapter.
   *
   * @param typeAllowedJpaRepository the Spring Data repository of allowed types.
   */
  public TypeAllowedRepositoryAdapter(TypeAllowedJpaRepository typeAllowedJpaRepository) {
    this.typeAllowedJpaRepository = Objects.requireNonNull(typeAllowedJpaRepository);
  }

  /**
   * Finds all the allowed types.
   *
   * @return all the allowed types.
   */
  @Override
  public TypesAllowed findAll() {
    return new TypesAllowed(
        typeAllowedJpaRepository.findAll().stream()
            .map(EntityMapper::toTypeAllowed)
            .collect(java.util.stream.Collectors.toSet()));
  }

  /**
   * Finds the allowed type with the given identifier.
   *
   * @param id the identifier of the allowed type.
   * @return the corresponding allowed type if any.
   */
  @Override
  public Optional<TypeAllowed> findById(long id) {
    return typeAllowedJpaRepository.findById(id).map(EntityMapper::toTypeAllowed);
  }

  /**
   * Saves the given allowed type.
   *
   * @param typeAllowed the allowed type to save.
   * @return the saved allowed type.
   */
  @Override
  @Transactional
  public TypeAllowed save(TypeAllowed typeAllowed) {
    var entity = new TypeAllowedEntity(typeAllowed.constructor(), typeAllowed.type());
    entity.setId(typeAllowed.id());
    var saved = typeAllowedJpaRepository.save(entity);
    return EntityMapper.toTypeAllowed(saved);
  }
}
