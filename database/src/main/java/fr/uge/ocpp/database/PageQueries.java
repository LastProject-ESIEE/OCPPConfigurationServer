package fr.uge.ocpp.database;

import fr.uge.ocpp.domain.models.PageQuery;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/** Translates the domain pagination queries into Spring Data pageables. */
@UtilityClass
public class PageQueries {

  /**
   * Builds a pageable sorting by identifier in the direction of the query.
   *
   * @param pageQuery the domain pagination query.
   * @return the corresponding pageable.
   */
  public Pageable byId(PageQuery pageQuery) {
    var direction = Sort.Direction.valueOf(pageQuery.sort().name());
    return PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(direction, "id"));
  }
}
