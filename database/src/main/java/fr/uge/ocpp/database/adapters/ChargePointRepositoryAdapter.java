package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.dao.ChargePointDao;
import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the charge points in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
@AllArgsConstructor
public class ChargePointRepositoryAdapter implements ChargePointRepository {

  private final ChargePointDao chargePointDao;

  /**
   * Finds the charge point with the given serial number and constructor.
   *
   * @param serialNumber the serial number of the charge point.
   * @param constructor the constructor of the charge point.
   * @return the corresponding charge point if any.
   */
  @Override
  public Optional<ChargePoint> findBySerialNumberChargepointAndConstructor(
      String serialNumber, String constructor) {
    return chargePointDao
        .findBySerialNumberChargepointAndConstructor(serialNumber, constructor)
        .map(ChargePointEntity::toDomain);
  }

  /**
   * Finds all the charge points ordered by descending identifier.
   *
   * @return all the charge points.
   */
  @Override
  public ChargePoints findAllByOrderByIdDesc() {
    return new ChargePoints(
        chargePointDao.findAllByOrderByIdDesc().stream().map(ChargePointEntity::toDomain).toList());
  }

  /**
   * Finds the charge points with a client id containing the given one, paginated.
   *
   * @param pageQuery the pagination query.
   * @param clientId the client id filter.
   * @return the corresponding page of charge points.
   */
  @Override
  public PageResult<ChargePoint> findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
      PageQuery pageQuery, String clientId) {
    var direction =
        pageQuery.sort() == fr.uge.ocpp.domain.models.Sort.ASC
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
    var pageable = PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(direction, "id"));
    var page =
        chargePointDao.findAllByClientIdContainingIgnoreCaseOrderByIdDesc(pageable, clientId);
    List<ChargePoint> content = page.stream().map(ChargePointEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Finds the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @return the corresponding charge point if any.
   */
  @Override
  public Optional<ChargePoint> findById(long id) {
    return chargePointDao.findById(id).map(ChargePointEntity::toDomain);
  }

  /**
   * Saves the given charge point.
   *
   * @param chargePoint the charge point to save.
   * @return the saved charge point.
   */
  @Override
  @Transactional
  public ChargePoint save(ChargePoint chargePoint) {
    ChargePointEntity entity = ChargePointEntity.fromDomain(chargePoint);
    return chargePointDao.save(entity).toDomain();
  }
}
