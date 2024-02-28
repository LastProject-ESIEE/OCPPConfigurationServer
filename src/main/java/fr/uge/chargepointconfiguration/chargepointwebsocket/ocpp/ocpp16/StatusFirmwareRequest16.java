package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;

/**
 * Sent by the chargepoint.<br>
 * This is a packet to give the current status of the firmware download.<br>
 * It should be responded with a {@link StatusFirmwareResponse16}.
 *
 * @param status The current status of the firmware download.
 */
public record StatusFirmwareRequest16(String status) implements OcppMessageRequest {
}
