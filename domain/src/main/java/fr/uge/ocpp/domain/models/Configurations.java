package fr.uge.ocpp.domain.models;

import java.util.List;
import java.util.Objects;

/** A list of configurations. */
public record Configurations(List<Configuration> configurationList) {

  /** Creates the list, defaulting to an empty list. */
  public Configurations {
    configurationList = Objects.requireNonNullElseGet(configurationList, List::of);
  }

  /**
   * Returns an empty list of configurations.
   *
   * @return an empty list of configurations.
   */
  public static Configurations empty() {
    return new Configurations(List.of());
  }
}
