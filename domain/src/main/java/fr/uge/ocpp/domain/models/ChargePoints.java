package fr.uge.ocpp.domain.models;

import java.util.List;
import java.util.Objects;

/** A list of charge points. */
public record ChargePoints(List<ChargePoint> chargePointList) {

  /** Creates the list, defaulting to an empty list. */
  public ChargePoints {
    chargePointList = Objects.requireNonNullElseGet(chargePointList, List::of);
  }
}
