package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.OcppMessageRequest;
import java.util.Objects;

/**
 * Packet sent by the machine to describe the current state of the firmware download.<br>
 * The server should respond by a {@link FirmwareStatusNotificationResponse16}.
 *
 * @param status An enum to describe the current status of the firmware download.
 */
public record FirmwareStatusNotificationRequest16(String status)
        implements OcppMessageRequest {

  /**
   * {@link FirmwareStatusNotificationRequest16}'s constructor.
   *
   * @param status An enum to describe the current status of the firmware download.
   */
  public FirmwareStatusNotificationRequest16 {
    Objects.requireNonNull(status);
  }
}
