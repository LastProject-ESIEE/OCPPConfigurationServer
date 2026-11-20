package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import java.util.Optional;

/** SPI port to persist and retrieve charge points. */
public interface ChargePointRepository {

  /**
   * Finds the charge point with the given serial number and constructor.
   *
   * @param serialNumber the serial number of the charge point.
   * @param constructor the constructor of the charge point.
   * @return the corresponding charge point if any.
   */
  Optional<ChargePoint> findBySerialNumberChargepointAndConstructor(
      String serialNumber, String constructor);

  /**
   * Finds all the charge points ordered by descending identifier.
   *
   * @return all the charge points.
   */
  ChargePoints findAllByOrderByIdDesc();

  /**
   * Finds all the charge points ordered by descending identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of charge points.
   */
  PageResult<ChargePoint> findAllByOrderByIdDesc(PageQuery pageQuery);

  /**
   * Finds the charge points matching the given request on serial number, type, constructor or
   * client id, paginated.
   *
   * @param request the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding page of charge points.
   */
  PageResult<ChargePoint> search(String request, PageQuery pageQuery);

  /**
   * Counts all the charge points.
   *
   * @return the number of charge points.
   */
  long count();

  /**
   * Counts the charge points matching the given request.
   *
   * @param request the search request.
   * @return the number of matching charge points.
   */
  long count(String request);

  /**
   * Finds the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @return the corresponding charge point if any.
   */
  Optional<ChargePoint> findById(long id);

  /**
   * Saves the given charge point.
   *
   * @param chargePoint the charge point to save.
   * @return the saved charge point.
   */
  ChargePoint save(ChargePoint chargePoint);
}
