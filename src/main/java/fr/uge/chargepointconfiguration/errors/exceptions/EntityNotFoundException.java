package fr.uge.chargepointconfiguration.errors.exceptions;

/**
 * HTTP Exception associated to 404 code.
 */
public class EntityNotFoundException extends RuntimeException {
  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(Throwable cause) {
    super(cause);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
