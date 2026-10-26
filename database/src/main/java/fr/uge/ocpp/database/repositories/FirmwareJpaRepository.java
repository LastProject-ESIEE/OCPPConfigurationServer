package fr.uge.ocpp.database.repositories;

import fr.uge.ocpp.database.entities.FirmwareEntity;
import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Spring Data access to the firmware table. */
@Repository
public interface FirmwareJpaRepository
    extends JpaRepository<FirmwareEntity, Long>, JpaSpecificationExecutor<FirmwareEntity> {

  /**
   * Finds the firmware with the given version.
   *
   * @param version the version of the firmware.
   * @return the corresponding firmware if any.
   */
  Optional<FirmwareEntity> findByVersion(String version);

  /**
   * Finds all the firmwares compatible with the given allowed type, ascending version.
   *
   * @param typeAllowed the allowed type filter.
   * @return the corresponding firmwares.
   */
  @Query("select f from FirmwareEntity f "
      + "join f.typesAllowed "
      + "where :typeAllowed member of f.typesAllowed order by f.version asc")
  List<FirmwareEntity> findAllByTypeAllowedAsc(@Param("typeAllowed") TypeAllowedEntity typeAllowed);

  /**
   * Finds all the firmwares compatible with the given allowed type, descending version.
   *
   * @param typeAllowed the allowed type filter.
   * @return the corresponding firmwares.
   */
  @Query("select f from FirmwareEntity f "
      + "join f.typesAllowed "
      + "where :typeAllowed member of f.typesAllowed order by f.version desc")
  List<FirmwareEntity> findAllByTypeAllowedDesc(@Param("typeAllowed")
      TypeAllowedEntity typeAllowed);

  /**
   * Finds all the firmwares ordered by descending identifier.
   *
   * @return all the firmwares.
   */
  List<FirmwareEntity> findAllByOrderByIdDesc();

  /**
   * Finds all the firmwares ordered by descending identifier, paginated.
   *
   * @param pageable the pagination information.
   * @return the corresponding page of firmwares.
   */
  Page<FirmwareEntity> findAllByOrderByIdDesc(Pageable pageable);

  /**
   * Finds the firmware with the given url.
   *
   * @param url the url of the firmware.
   * @return the corresponding firmware if any.
   */
  Optional<FirmwareEntity> findByUrl(String url);
}
