package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.PageQueries;
import fr.uge.ocpp.database.dao.ConfigurationDao;
import fr.uge.ocpp.database.entities.ConfigurationEntity;
import fr.uge.ocpp.database.specifications.ConfigurationSpecifications;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the configurations in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConfigurationRepositoryAdapter implements ConfigurationRepository {

  private final ConfigurationDao configurationDao;

  /**
   * Finds all the configurations ordered by descending identifier.
   *
   * @return all the configurations.
   */
  @Override
  public Configurations findAllByOrderByIdDesc() {
    return new Configurations(
        configurationDao.findAllByOrderByIdDesc().stream()
            .map(ConfigurationEntity::toDomain)
            .toList());
  }

  /**
   * Finds all the configurations ordered by descending identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of configurations.
   */
  @Override
  public PageResult<Configuration> findAllByOrderByIdDesc(PageQuery pageQuery) {
    var page = configurationDao.findAllByOrderByIdDesc(PageQueries.byId(pageQuery));
    List<Configuration> content = page.stream().map(ConfigurationEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Finds the configurations matching the given request on name or description, paginated.
   *
   * @param request the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding page of configurations.
   */
  @Override
  public PageResult<Configuration> search(String request, PageQuery pageQuery) {
    var page =
        configurationDao.findAll(
            ConfigurationSpecifications.request(request), PageQueries.byId(pageQuery));
    List<Configuration> content = page.stream().map(ConfigurationEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Counts the configurations matching the given request.
   *
   * @param request the search request.
   * @return the number of matching configurations.
   */
  @Override
  public long count(String request) {
    return configurationDao.count(ConfigurationSpecifications.request(request));
  }

  /**
   * Saves the given configuration.
   *
   * @param configuration the configuration to save.
   * @return the saved configuration.
   */
  @Override
  @Transactional
  public Configuration save(Configuration configuration) {
    ConfigurationEntity entity = ConfigurationEntity.fromDomain(configuration);
    return configurationDao.save(entity).toDomain();
  }

  /**
   * Finds the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration if any.
   */
  @Override
  public Optional<Configuration> findById(long id) {
    return configurationDao.findById(id).map(ConfigurationEntity::toDomain);
  }
}
