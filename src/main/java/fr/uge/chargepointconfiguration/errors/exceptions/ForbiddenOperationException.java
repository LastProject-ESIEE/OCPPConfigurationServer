package fr.uge.chargepointconfiguration.errors.exceptions;

/**
 * HTTP Exception associated to 403 code.
 */
public class ForbiddenOperationException extends RuntimeException {
  public ForbiddenOperationException(String message) {
    super(message);
  }

  public ForbiddenOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ForbiddenOperationException(Throwable cause) {
    super(cause);
  }
}
