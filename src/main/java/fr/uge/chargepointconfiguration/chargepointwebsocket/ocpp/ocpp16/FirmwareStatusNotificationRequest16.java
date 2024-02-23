package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

/**
 * Packet sent by the machine to describe the current state of the firmware download.<br>
 * The server should respond by a {@link FirmwareStatusNotificationResponse16}.
 *
 * @param firmwareStatus An enum to describe the current status of the firmware download.
 */
public record FirmwareStatusNotificationRequest16(String firmwareStatus) {
}
