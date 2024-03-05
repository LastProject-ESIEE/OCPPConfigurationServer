package fr.uge.chargepointconfiguration.chargepoint.notification;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.status.StatusDto;

/**
 * Charge point update websocket notification.
 */
public record ChargePointWebsocketNotification(int id, StatusDto status)
        implements WebSocketNotification {

  /**
   * Transform a charge point to the charge point update notification.
   *
   * @param chargepoint updated charge point.
   * @return charge point id and status
   */
  public static ChargePointWebsocketNotification from(Chargepoint chargepoint) {
    var statusDto = new StatusDto(
            chargepoint.getLastUpdate(),
            chargepoint.getError(),
            chargepoint.isState(),
            chargepoint.getStep(),
            chargepoint.getStatusProcess()
    );
    return new ChargePointWebsocketNotification(chargepoint.getId(), statusDto);
  }
}
