package fr.uge.ocpp.websocket.ocpp.ocpp2;

import fr.uge.ocpp.websocket.ocpp.OcppMessageRequest;
import fr.uge.ocpp.websocket.ocpp.ocpp2.data.Firmware;

/**
 * Represents a request for the machine to update the current firmware with the given firmware (URI
 * + retrieve time).
 *
 * @param requestId The Id of this request.
 * @param firmware {@link Firmware}.
 */
public record UpdateFirmwareRequest20(int requestId, Firmware firmware)
    implements OcppMessageRequest {}
