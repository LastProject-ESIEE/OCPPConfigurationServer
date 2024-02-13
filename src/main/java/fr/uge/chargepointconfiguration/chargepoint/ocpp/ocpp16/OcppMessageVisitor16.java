package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp16;

/**
 * Defines an OCPP 1.6 message for the visitor.
 */
public interface OcppMessageVisitor16 {
  void visit(BootNotificationRequest bootNotificationRequest);
}
