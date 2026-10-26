package fr.uge.ocpp.domain.exceptions;

/** Thrown when a domain entity does not exist. */
public class NotFoundException extends RuntimeException {

  /**
   * Creates a not found exception.
   *
   * @param message the detail message.
   */
  public NotFoundException(String message) {
    super(message);
  }
}
