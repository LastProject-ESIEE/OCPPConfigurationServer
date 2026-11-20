package fr.uge.ocpp.domain.models;

import fr.uge.ocpp.domain.exceptions.BadRequestException;
import java.util.Objects;

/** A pagination query. */
public record PageQuery(int page, int size, Sort sort) {

  /** Creates the query, validating page and size and defaulting the sort to descending. */
  public PageQuery {
    if (page < 0 || size < 1) {
      throw new BadRequestException("Page >= 0 et taille >= 1 requis");
    }
    sort = Objects.requireNonNullElse(sort, Sort.DESC);
  }
}
