package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.configuration.Configuration;
import fr.uge.chargepointconfiguration.firmware.Firmware;
import fr.uge.chargepointconfiguration.status.Status;
import java.sql.Timestamp;

/**
 * DTO to read configuration in database.
 *
 * @param id Database id of the chargepoint stored.
 * @param serialNumberChargepoint The chargepoint's unique serial id.
 * @param type The commercial name of the chargepoint.
 * @param constructor The chargepoint's manufacturer.
 * @param clientId The client's name of the chargepoint.
 * @param serverAddress The server's URL of the chargepoint.
 * @param configuration A JSON containing the chargepoint's configuration.
 * @param status {@link Status}.
 * @param firmware {@link Firmware}.
 */
public record ChargepointDto(
    int id,
    String serialNumberChargepoint,
    String type,
    String constructor,
    String clientId,
    String serverAddress,
    Configuration configuration,
    Status status,
    Firmware firmware) {
}
