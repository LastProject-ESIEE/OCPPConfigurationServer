package fr.uge.chargepointconfiguration.chargepoint.notification;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.status.Status;

/**
 * Charge point update websocket notification.
 */
public record ChargePointWebsocketNotification(int id, Status status)
        implements WebSocketNotification {

  /**
   * Transform a charge point to the charge point update notification.
   *
   * @param chargepoint updated charge point.
   * @return charge point id and status
   */
  public ChargePointWebsocketNotification from(Chargepoint chargepoint) {
    return new ChargePointWebsocketNotification(chargepoint.getId(), chargepoint.getStatus());
  }
}
