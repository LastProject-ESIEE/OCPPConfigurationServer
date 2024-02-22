package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageRequest;


/**
 * An OCCP 1.6 message to change a field value in the charge point configuration.
 *
 * @param key String.
 * @param value String.
 */
public record ChangeConfigurationRequest16(String key, String value) implements OcppMessageRequest {
}
