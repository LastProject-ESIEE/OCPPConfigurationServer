package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

/**
 * An OCCP message to change a field value.
 *
 * @param key String.
 * @param value String.
 */
public record ChangeConfiguration(String key, String value) implements OcppMessage {
}
