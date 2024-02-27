package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageResponse;
import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.RegistrationStatus;

/**
 * Confirms if the {@link ResetRequest16} has been accepted or not.<br>
 * It should be sent after a {@link ResetRequest16}.
 *
 * @param status {@link RegistrationStatus}.
 */
public record ResetResponse16(RegistrationStatus status) implements OcppMessageResponse {
}
