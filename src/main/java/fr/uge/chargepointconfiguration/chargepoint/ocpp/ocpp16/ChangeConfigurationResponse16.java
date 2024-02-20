package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

/**
 * Charge point configuration change response.
 */
public record ChangeConfigurationResponse16(String status) implements OcppMessage {
}
