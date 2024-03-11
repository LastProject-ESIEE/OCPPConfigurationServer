package fr.uge.chargepointconfiguration.errors.exceptions;

/**
 * Exception to explicit that the entity is already existing.
 */
public class EntityAlreadyExistingException extends RuntimeException {
  public EntityAlreadyExistingException(String message) {
    super(message);
  }

  public EntityAlreadyExistingException(String message, Throwable cause) {
    super(message, cause);
  }

  public EntityAlreadyExistingException(Throwable cause) {
    super(cause);
  }
}
