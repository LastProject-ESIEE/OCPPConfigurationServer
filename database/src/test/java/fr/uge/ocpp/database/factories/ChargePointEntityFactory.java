package fr.uge.ocpp.database.factories;

import static fr.uge.ocpp.database.factories.ConfigurationEntityFactory.dummyConfigurationEntity;

import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import java.time.LocalDateTime;

/** Factory of dummy {@link ChargePointEntity} for tests. */
public class ChargePointEntityFactory {

  public static ChargePointEntity dummyChargePointEntity() {
    var entity =
        ChargePointEntity.builder()
            .serialNumberChargepoint("serial")
            .type("Type")
            .constructor("Const")
            .clientId("client-id")
            .configuration(dummyConfigurationEntity())
            .build();
    entity.setId(1L);
    entity.setLastUpdate(LocalDateTime.now());
    entity.setError("");
    entity.setState(false);
    entity.setStep(Step.FIRMWARE);
    entity.setStatus(StatusProcess.PENDING);
    return entity;
  }
}
