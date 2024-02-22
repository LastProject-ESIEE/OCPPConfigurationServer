package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.data;

/**
 * For every changed configuration, the machine sends this.<br>
 * This class represents the status of a variable change.
 *
 * @param attributeStatus Result status of setting the variable.
 * @param component {@link Component}.
 * @param variable {@link VariableType}.
 */
public record SetVariableResult(String attributeStatus,
                                Component component,
                                VariableType variable) {
}
