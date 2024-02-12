package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepoint.ocpp.OCPPMessage;

public record BootNotificationRequest(String chargePointVendor, String chargePointModel, String chargePointSerialNumber, String chargeBoxSerialNumber, String firmwareVersion) implements OCPPMessage {

}
