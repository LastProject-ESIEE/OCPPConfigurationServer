package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OCPPMessage;


public record BootNotificationResponse(String currentTime, int interval, RegistrationStatus status) implements OCPPMessage {
}
