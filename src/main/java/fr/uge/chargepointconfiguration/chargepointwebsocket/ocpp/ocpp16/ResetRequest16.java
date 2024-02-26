package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;

/**
 * A reset request sent by the server for the chargepoint.<br>
 * This request has a type which can be : Hard or Soft.<br>
 * Hard : Cancel every transaction and reboot.<br>
 * Soft : Finish every transaction and reboot.<br>
 * For now, we can use Hard reboot.
 *
 * @param type The reboot type (Hard or Soft).
 */
public record ResetRequest16(String type) implements OcppMessageRequest {
}
