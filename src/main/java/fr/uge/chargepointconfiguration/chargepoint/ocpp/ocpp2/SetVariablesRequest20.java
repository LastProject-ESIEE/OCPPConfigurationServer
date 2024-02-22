package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageRequest;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.data.SetVariableData;
import java.util.List;

/**
 * Request sent by the server to change the charge point configuration.
 *
 * @param setVariableData {@link SetVariableData}.
 */
public record SetVariablesRequest20(List<SetVariableData> setVariableData)
        implements OcppMessageRequest {
}
