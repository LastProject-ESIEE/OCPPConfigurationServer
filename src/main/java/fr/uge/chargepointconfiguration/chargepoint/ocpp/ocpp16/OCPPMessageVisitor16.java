package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

public interface OCPPMessageVisitor16 {
    void visit(BootNotificationRequest bootNotificationRequest);
}
