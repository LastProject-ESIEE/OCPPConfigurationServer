package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.data.ChargingStation;

/**
 * Defines the BootNotificationRequest message from the OCPP protocol.
 *
 * @param chargingStation ChargingStation information.
 * @param reason String, the reason of why this message has been sent.
 */
public record BootNotificationRequest20(ChargingStation chargingStation,
                                        String reason) implements OcppMessage {

}
