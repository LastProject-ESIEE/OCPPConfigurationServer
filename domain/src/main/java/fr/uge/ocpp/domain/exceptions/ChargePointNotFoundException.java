package fr.uge.ocpp.domain.exceptions;

public class ChargePointNotFoundException extends NotFoundException {
  public ChargePointNotFoundException(String message) {
    super(message);
  }
}
