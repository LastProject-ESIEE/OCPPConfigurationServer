package fr.uge.ocpp.domain.models;

import static fr.uge.ocpp.domain.models.ConfigurationFactory.dummyConfiguration;

public class ChargePointFactory {

  public static ChargePoint dummyChargePoint() {
    return new ChargePoint(1L, "serial", "Type", "Const", "client-id", null, "", false,
      Step.FIRMWARE, StatusProcess.PENDING, dummyConfiguration());
  }

  public static ChargePoint dummyChargePointWithoutConfiguration() {
    return new ChargePoint(1L, "serial", "Type", "Const", "client-id", null, "", false,
      Step.FIRMWARE, StatusProcess.PENDING, null);
  }
}
