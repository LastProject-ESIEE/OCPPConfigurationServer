package fr.uge.ocpp.domain.models;

import java.util.List;
import java.util.Objects;

public record ChargePoints(List<ChargePoint> chargePointList) {

  public ChargePoints {
    chargePointList = Objects.requireNonNullElseGet(chargePointList, List::of);
  }
}
