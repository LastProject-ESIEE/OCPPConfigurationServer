package fr.uge.ocpp.service.pagination;

import fr.uge.ocpp.domain.models.PageResult;
import java.util.List;
import java.util.function.Function;

/** A page of DTOs exposed by the API. */
public record PageDto<T>(List<T> content, int page, int size, long totalElements) {

  /**
   * Maps a domain page to the corresponding DTO page.
   *
   * @param result the domain page to map.
   * @param mapper the domain to DTO mapper.
   * @param <T> the domain type.
   * @param <D> the DTO type.
   * @return the corresponding DTO page.
   */
  public static <T, D> PageDto<D> of(PageResult<T> result, Function<T, D> mapper) {
    return new PageDto<>(
        result.content().stream().map(mapper).toList(),
        result.page(),
        result.size(),
        result.totalElements());
  }
}
