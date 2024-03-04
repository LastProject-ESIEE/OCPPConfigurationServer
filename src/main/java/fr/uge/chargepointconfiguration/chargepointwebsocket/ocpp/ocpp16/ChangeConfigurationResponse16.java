package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageResponse;
import java.util.Objects;

/**
 * Sent by the chargepoint after a {@link ChangeConfigurationRequest16}.
 *
 * @param status The current status of the change.<br>
 *               If the key has been accepted or not.
 */
public record ChangeConfigurationResponse16(String status) implements OcppMessageResponse {

  /**
   * {@link ChangeConfigurationResponse16}'s constructor.
   *
   * @param status The current status of the change.<br>
   *               If the key has been accepted or not.
   */
  public ChangeConfigurationResponse16 {
    Objects.requireNonNull(status);
  }
}
