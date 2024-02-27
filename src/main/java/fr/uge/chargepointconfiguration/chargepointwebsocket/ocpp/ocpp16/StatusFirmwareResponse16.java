package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;

/**
 * Sent by the server to the chargepoint.<br>
 * With this packet, the server acknowledge the current state of the firmware download.
 * It is sent after a {@link StatusFirmwareRequest16}.
 */
public record StatusFirmwareResponse16() implements OcppMessageRequest {
}
