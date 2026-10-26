package fr.uge.ocpp.service.chargepoint;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.Configuration;

/** Maps the charge point DTOs to the domain models. */
final class ChargePointMapper {

  /** Creates a new mapper, this class should not be instantiated. */
  private ChargePointMapper() {}

  /**
   * Maps the creation values to a charge point domain model.
   *
   * @param dto the creation values.
   * @return the corresponding domain model.
   */
  static ChargePoint toChargePoint(CreateChargePointDto dto) {
    Configuration configuration = null;
    if (dto.configuration() != null && dto.configuration() != CreateChargePointDto.NO_CONFIG_ID) {
      configuration = new Configuration(dto.configuration(), null, null, null, null, null);
    }
    return ChargePoint.of(
        dto.serialNumberChargepoint(),
        dto.type(),
        dto.constructor(),
        dto.clientId(),
        configuration);
  }
}
