package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageRequest;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.data.Firmware;

/**
 * Represents a request for the machine to update the current firmware
 * with the given firmware (URI + retrieve time).
 *
 * @param requestId The Id of this request.
 * @param firmware {@link Firmware}.
 */
public record UpdateFirmwareRequest20(int requestId,
                                      Firmware firmware) implements OcppMessageRequest {
}
