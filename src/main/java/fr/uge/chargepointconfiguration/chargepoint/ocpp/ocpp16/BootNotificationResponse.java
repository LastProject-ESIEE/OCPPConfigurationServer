package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

/**
 * Defines a response to a BootNotification message.
 *
 * @param currentTime String.
 * @param interval An int.
 * @param status RegistrationStatus.
 */
public record BootNotificationResponse(String currentTime,
                                       int interval,
                                       RegistrationStatus status) implements OcppMessage {
}
