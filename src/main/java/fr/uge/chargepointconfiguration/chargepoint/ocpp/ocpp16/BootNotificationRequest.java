package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

/**
 * Defines the BootNotificationRequest message from the OCPP protocol.
 *
 * @param chargePointVendor String.
 * @param chargePointModel String.
 * @param chargePointSerialNumber String.
 * @param chargeBoxSerialNumber String.
 * @param firmwareVersion String.
 */
public record BootNotificationRequest(String chargePointVendor,
                                      String chargePointModel,
                                      String chargePointSerialNumber,
                                      String chargeBoxSerialNumber,
                                      String firmwareVersion) implements OcppMessage {

}
