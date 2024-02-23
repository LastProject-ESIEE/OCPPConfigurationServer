package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;

/**
 * Defines the BootNotificationRequest message from the OCPP protocol.
 *
 * @param chargePointVendor String.
 * @param chargePointModel String.
 * @param chargePointSerialNumber String.
 * @param chargeBoxSerialNumber String.
 * @param firmwareVersion String.
 */
public record BootNotificationRequest16(String chargePointVendor,
                                        String chargePointModel,
                                        String chargePointSerialNumber,
                                        String chargeBoxSerialNumber,
                                        String firmwareVersion) implements OcppMessageRequest {

}
