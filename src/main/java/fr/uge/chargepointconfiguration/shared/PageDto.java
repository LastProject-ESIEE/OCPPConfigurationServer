package fr.uge.chargepointconfiguration.shared;

import java.util.List;
import java.util.Objects;


/**
 * A record to represent a page of data.
 *
 * @param total total amount of element
 * @param page which page is displayed
 * @param size asked size of the page (not necessarily effective size)
 * @param data list of T containing the actual data
 * @param next string representing the url path with options to find the next page
 *
 * @param <T> The type of data it is containing
 */
public record PageDto<T>(
      long total,
      int page,
      int size,
      List<T> data,
      String next
) {

  /**
   * Default constructor for a page containing a list of T.
   *
   * @param total total amount of element
   * @param page which page is displayed
   * @param size asked size of the page (not necessarily effective size)
   * @param data list of T containing the actual data
   * @param next string representing the url path with options to find the next page
   */
  public PageDto {
    Objects.requireNonNull(data);
    Objects.requireNonNull(next);
    if (total < 0 || page < 0 || size < 0) {
      throw new IllegalArgumentException("Illegal negative value.");
    }
  }
}
