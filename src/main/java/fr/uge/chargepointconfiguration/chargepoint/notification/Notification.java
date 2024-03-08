package fr.uge.chargepointconfiguration.chargepoint.notification;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.status.StatusDto;
import java.util.Objects;
import java.util.Optional;

/**
 * Defines the notification sent for the front through the websocket.
 *
 * @param name  The notification's name.
 * @param value {@link WebSocketNotification}.
 */
public record Notification(String name, WebSocketNotification value) {

  /**
   * {@link Notification}'s constructor.
   *
   * @param name  The notification's name.
   * @param value {@link WebSocketNotification}.
   */
  public Notification {
    Objects.requireNonNull(name);
    Objects.requireNonNull(value);
  }

  /**
   * Parses a notification for the toast.
   *
   * @param chargepoint The current chargepoint.
   * @param className The class name.
   * @return An optional of Notification.
   */
  public static Optional<Notification> from(Chargepoint chargepoint,
                                            String className) {
    return switch (className) {
      case "CriticalityWebsocketNotification" -> {
        var currentStep = chargepoint.getStep();
        if (chargepoint.getStatusProcess() == Chargepoint.StatusProcess.FAILED) {
          var message = new CriticalityWebsocketNotification(
                  chargepoint.getClientId(),
                  CriticalityWebsocketNotification.Type.ERROR,
                  "Échec "
                          + (currentStep == Chargepoint.Step.FIRMWARE ? "du " : "de ")
                          + currentStep
          );
          yield Optional.of(new Notification("CriticalityWebsocketNotification",
                  message));
        } else if (chargepoint.getStatusProcess() == Chargepoint.StatusProcess.FINISHED) {
          var message = new CriticalityWebsocketNotification(
                  chargepoint.getClientId(),
                  CriticalityWebsocketNotification.Type.INFO,
                  "Réussite "
                          + (currentStep == Chargepoint.Step.FIRMWARE ? "du " : "de ")
                          + currentStep
          );
          yield Optional.of(new Notification("CriticalityWebsocketNotification",
                  message));
        } else {
          if (currentStep == Chargepoint.Step.CONFIGURATION) {
            var message = new CriticalityWebsocketNotification(
                    chargepoint.getClientId(),
                    CriticalityWebsocketNotification.Type.INFO,
                    "Réussite du " + currentStep
            );
            yield Optional.of(new Notification("CriticalityWebsocketNotification",
                    message));
          }
        }
        yield Optional.empty();
      }
      case "ChargePointWebsocketNotification" -> {
        var statusDto = new StatusDto(
                chargepoint.getLastUpdate(),
                chargepoint.getError(),
                chargepoint.isState(),
                chargepoint.getStep(),
                chargepoint.getStatusProcess()
        );
        var chargepointNotification = new ChargePointWebsocketNotification(chargepoint.getId(),
                statusDto);
        yield Optional.of(new Notification("ChargePointWebsocketNotification",
                chargepointNotification));
      }
      default ->
        Optional.empty();
    };
  }
}
