package fr.uge.chargepointconfiguration.chargepoint.notification;

/**
 * A message sent for the toast in the front.
 *
 * @param id The websocket message id.
 * @param content {@link CriticalityWebsocketContent}.
 */
public record CriticalityWebsocketNotification(int id, CriticalityWebsocketContent content)
        implements WebSocketNotification {
}
