package fr.uge.ocpp.websocket.ocpp.ocpp16;

import fr.uge.ocpp.websocket.ocpp.OcppMessageResponse;
import fr.uge.ocpp.websocket.ocpp.ocpp16.data.ConfigurationStatus;
import java.util.Objects;

/**
 * Sent by the chargepoint after a {@link ChangeConfigurationRequest16}.
 *
 * @param status The current status of the change.<br>
 *     If the key has been accepted or not.
 */
public record ChangeConfigurationResponse16(ConfigurationStatus status)
    implements OcppMessageResponse {

  /**
   * {@link ChangeConfigurationResponse16}'s constructor.
   *
   * @param status The current status of the change.<br>
   *     If the key has been accepted or not.
   */
  public ChangeConfigurationResponse16 {
    Objects.requireNonNull(status);
  }
}
