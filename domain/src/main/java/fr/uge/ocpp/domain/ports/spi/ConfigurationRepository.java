package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import java.util.Optional;

/** SPI port to persist and retrieve configurations. */
public interface ConfigurationRepository {

  /**
   * Finds all the configurations ordered by descending identifier.
   *
   * @return all the configurations.
   */
  Configurations findAllByOrderByIdDesc();

  /**
   * Finds the configurations ordered by descending identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of configurations.
   */
  PageResult<Configuration> findAllByOrderByIdDesc(PageQuery pageQuery);

  /**
   * Saves the given configuration.
   *
   * @param configuration the configuration to save.
   * @return the saved configuration.
   */
  Configuration save(Configuration configuration);

  /**
   * Finds the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration if any.
   */
  Optional<Configuration> findById(long id);
}
