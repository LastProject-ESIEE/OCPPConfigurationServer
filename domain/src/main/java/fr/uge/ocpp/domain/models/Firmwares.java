package fr.uge.ocpp.domain.models;

import java.util.List;
import java.util.Objects;

public record Firmwares(List<Firmware> firmwareList) {

  public Firmwares {
    firmwareList = Objects.requireNonNullElseGet(firmwareList, List::of);
  }
}
