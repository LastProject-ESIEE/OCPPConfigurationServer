package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.dao.FirmwareDao;
import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.database.entities.FirmwareEntity;
import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the firmwares in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FirmwareRepositoryAdapter implements FirmwareRepository {

  private final FirmwareDao firmwareDao;
  private final TypeAllowedDao typeAllowedDao;

  /**
   * Finds the firmware with the given version.
   *
   * @param version the version of the firmware.
   * @return the corresponding firmware if any.
   */
  @Override
  public Optional<Firmware> findByVersion(String version) {
    return firmwareDao.findByVersion(version).map(FirmwareEntity::toDomain);
  }

  /**
   * Finds all the firmwares compatible with the given allowed type, ascending version.
   *
   * @param typeAllowed the allowed type filter.
   * @return the corresponding firmwares.
   */
  @Override
  public Firmwares findAllByTypeAllowedAsc(TypeAllowed typeAllowed) {
    var entity =
        typeAllowedDao
            .findById(typeAllowed.id())
            .orElseGet(() -> TypeAllowedEntity.reference(typeAllowed.id()));
    return new Firmwares(
        firmwareDao.findAllByTypeAllowedAsc(entity).stream()
            .map(FirmwareEntity::toDomain)
            .toList());
  }

  /**
   * Finds all the firmwares compatible with the given allowed type, descending version.
   *
   * @param typeAllowed the allowed type filter.
   * @return the corresponding firmwares.
   */
  @Override
  public Firmwares findAllByTypeAllowedDesc(TypeAllowed typeAllowed) {
    var entity =
        typeAllowedDao
            .findById(typeAllowed.id())
            .orElseGet(() -> TypeAllowedEntity.reference(typeAllowed.id()));
    return new Firmwares(
        firmwareDao.findAllByTypeAllowedDesc(entity).stream()
            .map(FirmwareEntity::toDomain)
            .toList());
  }

  /**
   * Finds all the firmwares ordered by descending identifier.
   *
   * @return all the firmwares.
   */
  @Override
  public Firmwares findAllByOrderByIdDesc() {
    return new Firmwares(
        firmwareDao.findAllByOrderByIdDesc().stream().map(FirmwareEntity::toDomain).toList());
  }

  /**
   * Finds all the firmwares ordered by descending identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of firmwares.
   */
  @Override
  public PageResult<Firmware> findAllByOrderByIdDesc(PageQuery pageQuery) {
    var pageable =
        PageRequest.of(pageQuery.page(), pageQuery.size(), Sort.by(Sort.Direction.DESC, "id"));
    var page = firmwareDao.findAllByOrderByIdDesc(pageable);
    List<Firmware> content = page.stream().map(FirmwareEntity::toDomain).toList();
    return new PageResult<>(content, pageQuery.page(), pageQuery.size(), page.getTotalElements());
  }

  /**
   * Finds the firmware with the given url.
   *
   * @param url the url of the firmware.
   * @return the corresponding firmware if any.
   */
  @Override
  public Optional<Firmware> findByUrl(String url) {
    return firmwareDao.findByUrl(url).map(FirmwareEntity::toDomain);
  }

  /**
   * Finds the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware if any.
   */
  @Override
  public Optional<Firmware> findById(long id) {
    return firmwareDao.findById(id).map(FirmwareEntity::toDomain);
  }

  /**
   * Saves the given firmware.
   *
   * @param firmware the firmware to save.
   * @return the saved firmware.
   */
  @Override
  @Transactional
  public Firmware save(Firmware firmware) {
    FirmwareEntity entity = FirmwareEntity.fromDomain(firmware);
    return firmwareDao.save(entity).toDomain();
  }
}
