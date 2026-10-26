package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.database.repositories.FirmwareJpaRepository;
import fr.uge.ocpp.database.repositories.TypeAllowedJpaRepository;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Adapter persisting the firmwares in PostgreSQL through JPA. */
@Repository
@Transactional(readOnly = true)
public class FirmwareRepositoryAdapter implements FirmwareRepository {

  private final FirmwareJpaRepository firmwareJpaRepository;
  private final TypeAllowedJpaRepository typeAllowedJpaRepository;

  /**
   * Creates the adapter.
   *
   * @param firmwareJpaRepository the Spring Data repository of firmwares.
   * @param typeAllowedJpaRepository the Spring Data repository of allowed types.
   */
  public FirmwareRepositoryAdapter(
      FirmwareJpaRepository firmwareJpaRepository,
      TypeAllowedJpaRepository typeAllowedJpaRepository) {
    this.firmwareJpaRepository = Objects.requireNonNull(firmwareJpaRepository);
    this.typeAllowedJpaRepository = Objects.requireNonNull(typeAllowedJpaRepository);
  }

  /**
   * Finds the firmware with the given version.
   *
   * @param version the version of the firmware.
   * @return the corresponding firmware if any.
   */
  @Override
  public Optional<Firmware> findByVersion(String version) {
    return firmwareJpaRepository.findByVersion(version).map(EntityMapper::toFirmware);
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
        typeAllowedJpaRepository
            .findById(typeAllowed.id())
            .orElseGet(() -> TypeAllowedEntity.reference(typeAllowed.id()));
    return new Firmwares(
        firmwareJpaRepository.findAllByTypeAllowedAsc(entity).stream()
            .map(EntityMapper::toFirmware)
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
        typeAllowedJpaRepository
            .findById(typeAllowed.id())
            .orElseGet(() -> TypeAllowedEntity.reference(typeAllowed.id()));
    return new Firmwares(
        firmwareJpaRepository.findAllByTypeAllowedDesc(entity).stream()
            .map(EntityMapper::toFirmware)
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
        firmwareJpaRepository.findAllByOrderByIdDesc().stream()
            .map(EntityMapper::toFirmware)
            .toList());
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
    var page = firmwareJpaRepository.findAllByOrderByIdDesc(pageable);
    List<Firmware> content = page.stream().map(EntityMapper::toFirmware).toList();
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
    return firmwareJpaRepository.findByUrl(url).map(EntityMapper::toFirmware);
  }

  /**
   * Finds the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware if any.
   */
  @Override
  public Optional<Firmware> findById(long id) {
    return firmwareJpaRepository.findById(id).map(EntityMapper::toFirmware);
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
    var entity = EntityMapper.toFirmwareEntity(firmware, typeAllowedJpaRepository);
    var saved = firmwareJpaRepository.save(entity);
    return EntityMapper.toFirmware(saved);
  }
}
