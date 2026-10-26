package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A charge point of the OCPP configuration server domain.
 *
 * @param id             the charge point identifier.
 * @param serialNumber   the serial number of the charge point.
 * @param type           the commercial name of the charge point.
 * @param constructor    the constructor of the charge point.
 * @param clientId       the client identifier used by the charge point.
 * @param lastUpdate     the last time the charge point has been updated.
 * @param error           the error message in case of a failure.
 * @param state           true if the charge point is connected, false otherwise.
 * @param step            the current step of the ongoing process.
 * @param statusProcess   the status of the ongoing process.
 * @param configuration   the configuration applied to the charge point.
 */
public record ChargePoint(Long id,
                          String serialNumber,
                          String type,
                          String constructor,
                          String clientId,
                          LocalDateTime lastUpdate,
                          String error,
                          boolean state,
                          Step step,
                          StatusProcess statusProcess,
                          Configuration configuration) {

  /**
   * Creates a new charge point with the given identity and configuration.
   *
   * @param serialNumber the serial number of the charge point.
   * @param type         the commercial name of the charge point.
   * @param constructor  the constructor of the charge point.
   * @param clientId     the client identifier used by the charge point.
   * @param configuration the configuration applied to the charge point.
   * @return a new charge point.
   */
  public static ChargePoint of(String serialNumber,
                               String type,
                               String constructor,
                               String clientId,
                               Configuration configuration) {
    return new ChargePoint(
        null,
        serialNumber,
        type,
        constructor,
        clientId,
        null,
        null,
        false,
        null,
        null,
        configuration
    );
  }

  /**
   * Returns a copy of this charge point with the given identifier.
   *
   * @param id the new identifier.
   * @return a copy of this charge point.
   */
  public ChargePoint withId(Long id) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given serial number.
   *
   * @param serialNumber the new serial number.
   * @return a copy of this charge point.
   */
  public ChargePoint withSerialNumber(String serialNumber) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given type.
   *
   * @param type the new type.
   * @return a copy of this charge point.
   */
  public ChargePoint withType(String type) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given constructor.
   *
   * @param constructor the new constructor.
   * @return a copy of this charge point.
   */
  public ChargePoint withConstructor(String constructor) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given client identifier.
   *
   * @param clientId the new client identifier.
   * @return a copy of this charge point.
   */
  public ChargePoint withClientId(String clientId) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given last update timestamp.
   *
   * @param lastUpdate the new last update timestamp.
   * @return a copy of this charge point.
   */
  public ChargePoint withLastUpdate(LocalDateTime lastUpdate) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given configuration.
   *
   * @param configuration the new configuration.
   * @return a copy of this charge point.
   */
  public ChargePoint withConfiguration(Configuration configuration) {
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }

  /**
   * Returns a copy of this charge point with the given process status.
   *
   * @param step          the new step of the ongoing process.
   * @param statusProcess the new status of the ongoing process.
   * @return a copy of this charge point.
   */
  public ChargePoint withStatus(Step step, StatusProcess statusProcess) {
    Objects.requireNonNull(step);
    Objects.requireNonNull(statusProcess);
    return new ChargePoint(id, serialNumber, type, constructor, clientId, lastUpdate,
        error, state, step, statusProcess, configuration);
  }
}
