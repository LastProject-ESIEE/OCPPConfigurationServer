package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.RegistrationStatus;

/**
 * Defines a response to the BootNotificationRequest.<br>
 * According to the OCPP documentation, the server should send a BootNotificationResponse
 * to confirm that we had read the message.<br>
 * See this as an ACK packet.
 *
 * @param currentTime The current server time of the server in String.
 * @param interval This value defines the interval in second that the remote should respect
 *                 before sending a HeartBeat packet (which is like a PING).
 * @param status This enum serves as a response for the message. If it is accepted,
 *               rejected or something else.
 */
public record BootNotificationResponse(String currentTime,
                                       int interval,
                                       RegistrationStatus status) implements OcppMessage {
}
