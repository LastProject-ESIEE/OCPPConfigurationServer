package fr.uge.ocpp.domain.exceptions;

public class ConfigurationNotFoundException extends NotFoundException {
  public ConfigurationNotFoundException(String message) {
    super(message);
  }
}
