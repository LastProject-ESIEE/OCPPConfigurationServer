package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.data.SetVariableDataType;
import java.util.List;

/**
 * A message sent to the machine to change the configuration in OCPP 2.0.
 */
public record ChangeConfigurationRequest(List<SetVariableDataType> list) {
}
