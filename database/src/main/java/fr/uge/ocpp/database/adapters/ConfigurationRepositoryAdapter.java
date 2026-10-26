package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.entities.ConfigurationEntity;
import fr.uge.ocpp.database.repositories.ConfigurationJpaRepository;
import fr.uge.ocpp.database.repositories.FirmwareJpaRepository;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the configurations in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
public class ConfigurationRepositoryAdapter
    implements ConfigurationRepository {

  private final ConfigurationJpaRepository configurationJpaRepository;
  private final FirmwareJpaRepository firmwareJpaRepository;

  /**
   * Creates the adapter.
   *
   * @param configurationJpaRepository the Spring Data repository of configurations.
   * @param firmwareJpaRepository      the Spring Data repository of firmwares.
   */
  public ConfigurationRepositoryAdapter(ConfigurationJpaRepository configurationJpaRepository,
      FirmwareJpaRepository firmwareJpaRepository) {
    this.configurationJpaRepository = Objects.requireNonNull(configurationJpaRepository);
    this.firmwareJpaRepository = Objects.requireNonNull(firmwareJpaRepository);
  }

  /**
   * Finds all the configurations ordered by descending identifier.
   *
   * @return all the configurations.
   */
  @Override
  public Configurations findAllByOrderByIdDesc() {
    return new Configurations(configurationJpaRepository.findAllByOrderByIdDesc().stream()
        .map(EntityMapper::toConfiguration).toList());
  }

  /**
   * Finds all the configurations ordered by descending identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of configurations.
   */
  @Override
  public PageResult<Configuration> findAllByOrderByIdDesc(PageQuery pageQuery) {
    var pageable = PageRequest.of(pageQuery.page(), pageQuery.size(),
        Sort.by(Sort.Direction.DESC, "id"));
    var page = configurationJpaRepository.findAllByOrderByIdDesc(pageable);
    List<Configuration> content = page.stream().map(EntityMapper::toConfiguration).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
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
    var entity = EntityMapper.toConfigurationEntity(configuration, firmwareJpaRepository);
    ConfigurationEntity saved = configurationJpaRepository.save(entity);
    return EntityMapper.toConfiguration(saved);
  }

  /**
   * Finds the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration if any.
   */
  @Override
  public Optional<Configuration> findById(long id) {
    return configurationJpaRepository.findById(id).map(EntityMapper::toConfiguration);
  }
}
