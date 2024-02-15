package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.RegistrationStatus;

/**
 * Defines a response to a BootNotification message.
 *
 * @param currentTime String.
 * @param interval An int.
 * @param status RegistrationStatusEnumType.
 */
public record BootNotificationResponse(String currentTime,
                                       int interval,
                                       RegistrationStatus status) implements OcppMessage {
}
