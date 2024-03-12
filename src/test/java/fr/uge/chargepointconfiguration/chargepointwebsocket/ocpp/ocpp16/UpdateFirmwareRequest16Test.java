package fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp16;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JUnit test class for the {@link UpdateFirmwareRequest16}.
 */
class UpdateFirmwareRequest16Test {

  /**
   * Should not throw an exception when instantiating the record.
   */
  @Test
  public void correctConstructorShouldNotThrowException() {
    assertDoesNotThrow(() -> {
      new UpdateFirmwareRequest16(
              "location",
              LocalDateTime.now().toString()
      );
    });
  }

  /**
   * Should return the correct location.
   */
  @Test
  public void returnsCorrectLocation() {
    var test = new UpdateFirmwareRequest16(
            "location",
            LocalDateTime.now().toString()
    );
    assertEquals("location", test.location());
  }

  /**
   * Should return the correct retrieve date.
   */
  @Test
  public void returnsCorrectRetrieveDate() {
    var date = LocalDateTime.now().toString();
    var test = new UpdateFirmwareRequest16(
            "location",
            date
    );
    assertEquals(date, test.retrieveDate());
  }

  /**
   * Should throw a {@link NullPointerException} if the location is null.
   */
  @Test
  public void throwsExceptionIfLocationIsNull() {
    assertThrows(NullPointerException.class, () -> new UpdateFirmwareRequest16(
            null,
            LocalDateTime.now().toString()
    ));
  }

  /**
   * Should throw a {@link NullPointerException} if the retrieve date is null.
   */
  @Test
  public void throwsExceptionIfRetrieveDateIsNull() {
    assertThrows(NullPointerException.class, () -> new UpdateFirmwareRequest16(
            "location",
            null
    ));
  }
}