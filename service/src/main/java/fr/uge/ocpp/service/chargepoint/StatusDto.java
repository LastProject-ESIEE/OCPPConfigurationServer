package fr.uge.ocpp.service.chargepoint;

import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import java.sql.Timestamp;

/** The status of a charge point exposed by the API. */
public record StatusDto(Timestamp lastUpdate,
                        String error,
                        boolean state,
                        Step step,
                        StatusProcess status) {
}
