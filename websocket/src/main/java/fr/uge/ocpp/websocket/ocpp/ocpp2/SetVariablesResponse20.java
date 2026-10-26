package fr.uge.ocpp.websocket.ocpp.ocpp2;

import fr.uge.ocpp.websocket.ocpp.OcppMessageResponse;
import fr.uge.ocpp.websocket.ocpp.ocpp2.data.SetVariableResult;
import java.util.List;

/**
 * Request sent by the server to change the charge point configuration.
 *
 * @param setVariableResult {@link SetVariableResult}.
 */
public record SetVariablesResponse20(List<SetVariableResult> setVariableResult)
        implements OcppMessageResponse {
}
