package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;
import java.util.Objects;

/**
 * Defines the BootNotificationRequest sent by the chargepoint.<br>
 * The server should answer with a {@link BootNotificationResponse16}.
 *
 * @param chargePointVendor The manufacturer of the chargepoint.
 * @param chargePointModel The chargepoint's commercial name.
 * @param chargePointSerialNumber The serial id of the chargepoint.
 * @param chargeBoxSerialNumber The special name of the chargepoint.
 * @param firmwareVersion The firmware version of the chargepoint.
 */
public record BootNotificationRequest16(String chargePointVendor,
                                        String chargePointModel,
                                        String chargePointSerialNumber,
                                        String chargeBoxSerialNumber,
                                        String firmwareVersion) implements OcppMessageRequest {

  /**
   * {@link BootNotificationRequest16}'s constructor.
   *
   * @param chargePointVendor The manufacturer of the chargepoint.
   * @param chargePointModel The chargepoint's commercial name.
   * @param chargePointSerialNumber The serial id of the chargepoint.
   * @param chargeBoxSerialNumber The special name of the chargepoint.
   * @param firmwareVersion The firmware version of the chargepoint.
   */
  public BootNotificationRequest16 {
    Objects.requireNonNull(chargePointVendor);
    Objects.requireNonNull(chargePointModel);
    Objects.requireNonNull(chargeBoxSerialNumber);
    Objects.requireNonNull(chargeBoxSerialNumber);
    Objects.requireNonNull(firmwareVersion);
  }

}
