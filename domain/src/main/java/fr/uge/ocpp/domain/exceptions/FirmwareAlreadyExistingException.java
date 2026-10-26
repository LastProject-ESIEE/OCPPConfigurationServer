package fr.uge.ocpp.domain.exceptions;

/** Thrown when a firmware already exists. */
public class FirmwareAlreadyExistingException extends AlreadyExistingException {

  /**
   * Creates a firmware already existing exception.
   *
   * @param message the detail message.
   */
  public FirmwareAlreadyExistingException(String message) {
    super(message);
  }
}
