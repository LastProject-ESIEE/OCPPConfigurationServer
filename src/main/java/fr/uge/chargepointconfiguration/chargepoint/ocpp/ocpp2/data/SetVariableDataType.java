package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.data;

/**
 * Defines the type used in the ChangeConfigurationSRequest.
 *
 * @param attributeValue The given value to the variable.
 * @param component The variable's component.
 * @param variableType The component.
 */
public record SetVariableDataType(String attributeValue,
                                  Component component,
                                  VariableType variableType) {
}
