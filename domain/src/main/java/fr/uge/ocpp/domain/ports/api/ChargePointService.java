package fr.uge.ocpp.domain.ports.api;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;

/** API port exposing the charge point use cases. */
public interface ChargePointService {

  /**
   * Creates a charge point.
   *
   * @param chargePoint the charge point to create.
   * @return the created charge point.
   */
  ChargePoint save(ChargePoint chargePoint);

  /**
   * Finds all the charge points.
   *
   * @return all the charge points.
   */
  ChargePoints findAll();

  /**
   * Finds the charge points matching the given request, paginated.
   *
   * @param request the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding charge points.
   */
  ChargePoints findAll(String request, PageQuery pageQuery);

  /**
   * Finds the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @return the corresponding charge point.
   */
  ChargePoint findById(long id);

  /**
   * Counts the charge points matching the given request.
   *
   * @param request the search request.
   * @return the number of matching charge points.
   */
  long countTotalWithFilter(String request);

  /**
   * Counts all the charge points.
   *
   * @return the number of charge points.
   */
  long count();

  /**
   * Updates the charge point with the given identifier.
   *
   * @param id the identifier of the charge point to update.
   * @param chargePoint the new values of the charge point.
   * @return the updated charge point.
   */
  ChargePoint update(long id, ChargePoint chargePoint);
}
