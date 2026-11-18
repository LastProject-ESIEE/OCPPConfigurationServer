package fr.uge.ocpp.database.factories;

import static fr.uge.ocpp.database.factories.ConfigurationFactory.dummyConfiguration;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;

/** Factory of dummy {@link ChargePoint} domain models for tests. */
public class ChargePointFactory {

  public static ChargePoint dummyChargePoint() {
    return new ChargePoint(
        1L,
        "serial",
        "Type",
        "Const",
        "client-id",
        null,
        "",
        false,
        Step.FIRMWARE,
        StatusProcess.PENDING,
        dummyConfiguration());
  }
}
