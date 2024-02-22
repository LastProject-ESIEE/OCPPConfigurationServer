package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageResponse;

/**
 * Charge point configuration change response.
 */
public record ChangeConfigurationResponse16(String status) implements OcppMessageResponse {
}
