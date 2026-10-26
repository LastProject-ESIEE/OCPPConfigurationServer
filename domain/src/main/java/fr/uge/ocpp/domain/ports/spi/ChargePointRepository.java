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
   * @param constructor  the constructor of the charge point.
   * @return the corresponding charge point if any.
   */
  Optional<ChargePoint> findBySerialNumberChargepointAndConstructor(String serialNumber,
                                                                    String constructor);

  /**
   * Finds all the charge points ordered by descending identifier.
   *
   * @return all the charge points.
   */
  ChargePoints findAllByOrderByIdDesc();

  /**
   * Finds the charge points with a client id containing the given request, paginated.
   *
   * @param pageQuery the pagination query.
   * @param clientId  the client id filter.
   * @return the corresponding page of charge points.
   */
  PageResult<ChargePoint> findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
      PageQuery pageQuery, String clientId);

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
