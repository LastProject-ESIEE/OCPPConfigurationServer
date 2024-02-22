package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessageRequest;

/**
 * Represents the Update Firmware packet from OCPP 1.6.<br>
 * It should be answered by a {@link UpdateFirmwareResponse16}.
 *
 * @param location The URL of the firmware, it will be used by the machine
 *                 to download the firmware.
 * @param retrieveDate The date which the machine will download the firmware.
 */
public record UpdateFirmwareRequest16(String location, String retrieveDate)
        implements OcppMessageRequest {
}
