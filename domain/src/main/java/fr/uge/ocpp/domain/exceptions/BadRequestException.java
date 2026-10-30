package fr.uge.ocpp.domain.exceptions;

/** Thrown when input values violate the domain rules. */
public class BadRequestException extends RuntimeException {

  /**
   * Creates a bad request exception.
   *
   * @param message the detail message.
   */
  public BadRequestException(String message) {
    super(message);
  }
}
