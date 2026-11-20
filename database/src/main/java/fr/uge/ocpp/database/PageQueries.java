package fr.uge.ocpp.database;

import fr.uge.ocpp.domain.models.PageQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/** Translates the domain pagination queries into Spring Data pageables. */
public final class PageQueries {

  private PageQueries() {}

  /**
   * Builds a pageable sorting by identifier in the direction of the query.
   *
   * @param pageQuery the domain pagination query.
   * @return the corresponding pageable.
   */
  public static Pageable byId(PageQuery pageQuery) {
    var direction =
        pageQuery.sort() == fr.uge.ocpp.domain.models.Sort.ASC
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
    return PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(direction, "id"));
  }
}
