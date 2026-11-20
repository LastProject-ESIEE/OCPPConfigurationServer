package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the allowed types in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TypeAllowedRepositoryAdapter implements TypeAllowedRepository {

  private final TypeAllowedDao typeAllowedDao;

  /**
   * Finds all the allowed types.
   *
   * @return all the allowed types.
   */
  @Override
  public TypesAllowed findAll() {
    return new TypesAllowed(
        typeAllowedDao.findAll().stream()
            .map(TypeAllowedEntity::toDomain)
            .collect(java.util.stream.Collectors.toSet()));
  }

  /**
   * Finds all the allowed types ordered by identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of allowed types.
   */
  @Override
  public PageResult<TypeAllowed> findAllByOrderByIdDesc(PageQuery pageQuery) {
    var direction =
        pageQuery.sort() == fr.uge.ocpp.domain.models.Sort.ASC
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
    var pageable = PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(direction, "id"));
    var page = typeAllowedDao.findAllByOrderByIdDesc(pageable);
    List<TypeAllowed> content = page.stream().map(TypeAllowedEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Finds the allowed type with the given identifier.
   *
   * @param id the identifier of the allowed type.
   * @return the corresponding allowed type if any.
   */
  @Override
  public Optional<TypeAllowed> findById(long id) {
    return typeAllowedDao.findById(id).map(TypeAllowedEntity::toDomain);
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
    TypeAllowedEntity entity = TypeAllowedEntity.fromDomain(typeAllowed);
    return typeAllowedDao.save(entity).toDomain();
  }
}
