package fr.uge.ocpp.domain.ports.api;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;

public interface ChargePointService {

  ChargePoint save(ChargePoint chargePoint);

  ChargePoints findAll();

  ChargePoint findById(long id);

  ChargePoints findAll(String request, PageQuery pageQuery);

  long countTotalWithFilter(String request);

  long count();

  ChargePoint update(int id, ChargePoint chargePoint);

}
