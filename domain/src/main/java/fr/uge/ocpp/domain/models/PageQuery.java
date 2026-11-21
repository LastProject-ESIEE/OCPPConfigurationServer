package fr.uge.ocpp.domain.models;

import java.util.Objects;

/** A pagination query. */
public record PageQuery(int page, int size, Sort sort) {

  /** Creates the query, validating page and size and defaulting the sort to descending. */
  public PageQuery {
    if (page < 0 || size < 1) {
      throw new IllegalArgumentException("page >= 0 and size >= 1 required");
    }
    sort = Objects.requireNonNullElse(sort, Sort.DESC);
  }
}
