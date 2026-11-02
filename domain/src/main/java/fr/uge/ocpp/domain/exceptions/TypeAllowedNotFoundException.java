package fr.uge.ocpp.domain.exceptions;

/** Thrown when an allowed type does not exist. */
public class TypeAllowedNotFoundException extends NotFoundException {

  /**
   * Creates an allowed type not found exception.
   *
   * @param message the detail message.
   */
  public TypeAllowedNotFoundException(String message) {
    super(message);
  }
}
