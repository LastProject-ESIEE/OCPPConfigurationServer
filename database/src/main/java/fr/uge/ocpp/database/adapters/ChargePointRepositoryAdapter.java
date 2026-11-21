package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.PageQueries;
import fr.uge.ocpp.database.dao.ChargePointDao;
import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.database.specifications.ChargePointSpecifications;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the charge points in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
   * Finds all the charge points ordered by identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of charge points.
   */
  @Override
  public PageResult<ChargePoint> findAllByOrderByIdDesc(PageQuery pageQuery) {
    var page = chargePointDao.findAll(PageQueries.byId(pageQuery));
    List<ChargePoint> content = page.stream().map(ChargePointEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Finds the charge points matching the given request on serial number, type, constructor or
   * client id, paginated.
   *
   * @param request the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding page of charge points.
   */
  @Override
  public PageResult<ChargePoint> search(String request, PageQuery pageQuery) {
    var page =
        chargePointDao.findAll(
            ChargePointSpecifications.request(request), PageQueries.byId(pageQuery));
    List<ChargePoint> content = page.stream().map(ChargePointEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Counts the charge points matching the given request. A null or blank request counts all.
   *
   * @param request the search request, or null to count all.
   * @return the number of matching charge points.
   */
  @Override
  public long count(String request) {
    return chargePointDao.count(ChargePointSpecifications.request(request));
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
