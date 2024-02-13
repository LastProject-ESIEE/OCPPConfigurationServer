package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

public record ChangeConfiguration(String key, String value) implements OcppMessage {
}
