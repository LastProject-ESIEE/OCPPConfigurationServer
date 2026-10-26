package fr.uge.ocpp.domain.exceptions;

/** Thrown when a charge point already exists. */
public class ChargePointAlreadyExistingException extends AlreadyExistingException {

  /**
   * Creates a charge point already existing exception.
   *
   * @param message the detail message.
   */
  public ChargePointAlreadyExistingException(String message) {
    super(message);
  }
}
