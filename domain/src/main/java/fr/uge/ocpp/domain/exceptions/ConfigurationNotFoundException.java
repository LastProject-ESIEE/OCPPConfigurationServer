package fr.uge.ocpp.domain.exceptions;

/** Thrown when a configuration does not exist. */
public class ConfigurationNotFoundException extends NotFoundException {

  /**
   * Creates a configuration not found exception.
   *
   * @param message the detail message.
   */
  public ConfigurationNotFoundException(String message) {
    super(message);
  }
}
