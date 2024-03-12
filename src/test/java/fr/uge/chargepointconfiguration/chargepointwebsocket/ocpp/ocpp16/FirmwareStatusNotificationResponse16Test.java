package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * JUnit test class for the {@link FirmwareStatusNotificationResponse16}.
 */
class FirmwareStatusNotificationResponse16Test {

  /**
   * Should not throw an exception when instantiating the record.
   */
  @Test
  public void correctConstructorShouldNotThrowException() {
    assertDoesNotThrow(() -> {
      new FirmwareStatusNotificationResponse16();
    });
  }
}