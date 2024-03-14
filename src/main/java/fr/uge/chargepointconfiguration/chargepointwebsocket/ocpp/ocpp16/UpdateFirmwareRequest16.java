package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;
import java.util.Objects;

/**
 * Represents the Update Firmware packet from OCPP 1.6.<br>
 * It should be answered by a {@link UpdateFirmwareResponse16}.
 *
 * @param location     The URL of the firmware, it will be used by the machine
 *                     to download the firmware.
 * @param retrieveDate The date which the machine will download the firmware.
 */
public record UpdateFirmwareRequest16(String location, String retrieveDate)
    implements OcppMessageRequest {

  /**
   * {@link UpdateFirmwareRequest16}'s constructor.
   *
   * @param location     The URL of the firmware, it will be used by the machine
   *                     to download the firmware.
   * @param retrieveDate The date which the machine will download the firmware.
   */
  public UpdateFirmwareRequest16 {
    Objects.requireNonNull(location);
    Objects.requireNonNull(retrieveDate);
  }
}
