package fr.uge.ocpp.domain.models;

import java.util.List;
import java.util.Objects;

/** A list of firmwares. */
public record Firmwares(List<Firmware> firmwareList) {

  /** Creates the list, defaulting to an empty list. */
  public Firmwares {
    firmwareList = Objects.requireNonNullElseGet(firmwareList, List::of);
  }
}
