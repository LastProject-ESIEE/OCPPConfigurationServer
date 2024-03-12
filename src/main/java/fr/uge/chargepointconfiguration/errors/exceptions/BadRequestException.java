package fr.uge.chargepointconfiguration.errors.exceptions;

/**
 * HTTP Exception associated to 400 code.
 */
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadRequestException(Throwable cause) {
    super(cause);
  }
}
