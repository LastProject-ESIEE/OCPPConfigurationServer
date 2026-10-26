package fr.uge.ocpp.service.chargepoint;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.service.configuration.ConfigurationDto;
import java.sql.Timestamp;

/** A charge point exposed by the API. */
public record ChargePointDto(long id,
                             String serialNumberChargepoint,
                             String type,
                             String constructor,
                             String clientId,
                             ConfigurationDto configuration,
                             StatusDto status) {

  /**
   * Maps a charge point domain model to the corresponding DTO.
   *
   * @param chargePoint the domain model to map.
   * @return the corresponding DTO.
   */
  public static ChargePointDto of(ChargePoint chargePoint) {
    var configuration = chargePoint.configuration() == null ? null
        : ConfigurationDto.of(chargePoint.configuration());
    var status = new StatusDto(
        chargePoint.lastUpdate() == null ? null : Timestamp.valueOf(chargePoint.lastUpdate()),
        chargePoint.error(), chargePoint.state(), chargePoint.step(),
        chargePoint.statusProcess());
    return new ChargePointDto(chargePoint.id(), chargePoint.serialNumber(),
        chargePoint.type(), chargePoint.constructor(), chargePoint.clientId(), configuration,
        status);
  }
}
