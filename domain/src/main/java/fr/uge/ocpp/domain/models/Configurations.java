package fr.uge.ocpp.domain.models;

import java.util.List;
import java.util.Objects;

public record Configurations(List<Configuration> configurationList) {

  public Configurations {
    configurationList = Objects.requireNonNullElseGet(configurationList, List::of);
  }

  public static Configurations empty() {
    return new Configurations(List.of());
  }
}
