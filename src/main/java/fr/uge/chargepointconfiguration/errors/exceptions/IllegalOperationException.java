package fr.uge.chargepointconfiguration.errors.exceptions;

/**
 * HTTP Exception associated to 401 code.
 */
public class IllegalOperationException extends RuntimeException {
  public IllegalOperationException(String message) {
    super(message);
  }

  public IllegalOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalOperationException(Throwable cause) {
    super(cause);
  }
}
