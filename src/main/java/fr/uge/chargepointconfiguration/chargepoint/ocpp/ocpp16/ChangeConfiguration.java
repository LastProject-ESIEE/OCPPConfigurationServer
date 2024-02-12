package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OCPPMessage;

public record ChangeConfiguration(String key, String value) implements OCPPMessage {
}
