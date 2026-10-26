package fr.uge.ocpp.domain.ports.api;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;

/** API port exposing the configuration use cases. */
public interface ConfigurationService {

  /**
   * Creates a configuration.
   *
   * @param configuration the configuration to create.
   * @return the created configuration.
   */
  Configuration save(Configuration configuration);

  /**
   * Updates the configuration with the given identifier.
   *
   * @param id           the identifier of the configuration to update.
   * @param configuration the new values of the configuration.
   * @return the updated configuration.
   */
  Configuration update(long id, Configuration configuration);

  /**
   * Finds all the configurations.
   *
   * @return all the configurations.
   */
  Configurations findAll();

  /**
   * Finds the configuration with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return the corresponding configuration.
   */
  Configuration findById(long id);

  /**
   * Counts the configurations matching the given request.
   *
   * @param request the search request.
   * @return the number of matching configurations.
   */
  long countTotalWithFilter(String request);

  /**
   * Finds the configurations matching the given request, paginated.
   *
   * @param request   the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding configurations.
   */
  Configurations search(String request, PageQuery pageQuery);
}
