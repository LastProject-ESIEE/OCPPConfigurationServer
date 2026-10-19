package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;

public interface ChargePointRepository {

  ChargePoint findBySerialNumberChargepointAndConstructor(String serialNumber,
                                                          String constructor);

  ChargePoints findAllByOrderByIdDesc();

  PageResult<ChargePoint> findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
    PageQuery pageQuery,
    String clientId);
}
