package fr.uge.ocpp.domain.exceptions;

/** Thrown when a domain entity already exists. */
public class AlreadyExistingException extends RuntimeException {

  /**
   * Creates an already existing exception.
   *
   * @param message the detail message.
   */
  public AlreadyExistingException(String message) {
    super(message);
  }
}
