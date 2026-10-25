package fr.uge.ocpp.domain.exceptions;

public class FirmwareNotFoundException extends NotFoundException {
  public FirmwareNotFoundException(String message) {
    super(message);
  }
}
