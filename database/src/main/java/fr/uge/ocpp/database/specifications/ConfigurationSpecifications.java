package fr.uge.ocpp.database.specifications;

import fr.uge.ocpp.database.entities.ConfigurationEntity;
import org.springframework.data.jpa.domain.Specification;

/** Specifications for the configuration table. */
public final class ConfigurationSpecifications {

  private ConfigurationSpecifications() {}

  /**
   * Matches the configurations whose name or description contains the given request, ignoring
   * case. A blank request matches everything.
   *
   * @param request the search request.
   * @return the corresponding specification.
   */
  public static Specification<ConfigurationEntity> request(String request) {
    if (request == null || request.isBlank()) {
      return (root, query, cb) -> cb.conjunction();
    }
    var pattern = "%" + request.toLowerCase() + "%";
    return (root, query, cb) ->
        cb.or(
            cb.like(cb.lower(root.get("name")), pattern),
            cb.like(cb.lower(root.get("description")), pattern));
  }
}
