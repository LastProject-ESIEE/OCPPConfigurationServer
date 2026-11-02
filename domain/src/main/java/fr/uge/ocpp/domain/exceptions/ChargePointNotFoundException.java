package fr.uge.ocpp.domain.exceptions;

/** Thrown when a charge point does not exist. */
public class ChargePointNotFoundException extends NotFoundException {

  /**
   * Creates a charge point not found exception.
   *
   * @param message the detail message.
   */
  public ChargePointNotFoundException(String message) {
    super(message);
  }
}
