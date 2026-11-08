package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/**
 * A charge point of the OCPP configuration server domain.
 *
 * @param id the charge point identifier.
 * @param serialNumber the serial number of the charge point.
 * @param type the commercial name of the charge point.
 * @param constructor the constructor of the charge point.
 * @param clientId the client identifier used by the charge point.
 * @param lastUpdate the last time the charge point has been updated.
 * @param error the error message in case of a failure.
 * @param state true if the charge point is connected, false otherwise.
 * @param step the current step of the ongoing process.
 * @param statusProcess the status of the ongoing process.
 * @param configuration the configuration applied to the charge point.
 */
@Builder
public record ChargePoint(
    @With Long id,
    @With String serialNumber,
    @With String type,
    @With String constructor,
    @With String clientId,
    @With LocalDateTime lastUpdate,
    @With String error,
    @With boolean state,
    @With Step step,
    @With StatusProcess statusProcess,
    @With Configuration configuration) {

  /**
   * Creates a new charge point with the given identity and configuration.
   *
   * @param serialNumber the serial number of the charge point.
   * @param type the commercial name of the charge point.
   * @param constructor the constructor of the charge point.
   * @param clientId the client identifier used by the charge point.
   * @param configuration the configuration applied to the charge point.
   * @return a new charge point.
   */
  public static ChargePoint of(
      String serialNumber,
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
        configuration);
  }
}
