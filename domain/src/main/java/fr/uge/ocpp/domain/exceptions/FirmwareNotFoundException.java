package fr.uge.ocpp.domain.exceptions;

/** Thrown when a firmware does not exist. */
public class FirmwareNotFoundException extends NotFoundException {

  /**
   * Creates a firmware not found exception.
   *
   * @param message the detail message.
   */
  public FirmwareNotFoundException(String message) {
    super(message);
  }
}
