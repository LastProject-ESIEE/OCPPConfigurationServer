package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;
import java.util.Optional;

/** SPI port to persist and retrieve firmwares. */
public interface FirmwareRepository {

  /**
   * Finds the firmware with the given version.
   *
   * @param version the version of the firmware.
   * @return the corresponding firmware if any.
   */
  Optional<Firmware> findByVersion(String version);

  /**
   * Finds all the firmwares of the given allowed type, ordered by ascending type.
   *
   * @param typeAllowed the allowed type filter.
   * @return the corresponding firmwares.
   */
  Firmwares findAllByTypeAllowedAsc(TypeAllowed typeAllowed);

  /**
   * Finds all the firmwares of the given allowed type, ordered by descending type.
   *
   * @param typeAllowed the allowed type filter.
   * @return the corresponding firmwares.
   */
  Firmwares findAllByTypeAllowedDesc(TypeAllowed typeAllowed);

  /**
   * Finds all the firmwares ordered by descending identifier.
   *
   * @return all the firmwares.
   */
  Firmwares findAllByOrderByIdDesc();

  /**
   * Finds all the firmwares ordered by descending identifier, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of firmwares.
   */
  PageResult<Firmware> findAllByOrderByIdDesc(PageQuery pageQuery);

  /**
   * Finds the firmware with the given url.
   *
   * @param url the url of the firmware.
   * @return the corresponding firmware if any.
   */
  Optional<Firmware> findByUrl(String url);

  /**
   * Finds the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware if any.
   */
  Optional<Firmware> findById(long id);

  /**
   * Saves the given firmware.
   *
   * @param firmware the firmware to save.
   * @return the saved firmware.
   */
  Firmware save(Firmware firmware);
}
