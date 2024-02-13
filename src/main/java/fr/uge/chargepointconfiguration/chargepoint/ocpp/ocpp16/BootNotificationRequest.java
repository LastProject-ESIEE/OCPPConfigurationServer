package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OcppMessage;

public record BootNotificationRequest(String chargePointVendor, String chargePointModel, String chargePointSerialNumber, String chargeBoxSerialNumber, String firmwareVersion) implements OcppMessage {

}
