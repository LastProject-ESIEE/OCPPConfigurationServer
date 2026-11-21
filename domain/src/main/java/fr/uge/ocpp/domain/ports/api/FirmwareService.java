package fr.uge.ocpp.domain.ports.api;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;

/** API port exposing the firmware use cases. */
public interface FirmwareService {

  /**
   * Creates a firmware.
   *
   * @param firmware the firmware to create.
   * @return the created firmware.
   */
  Firmware save(Firmware firmware);

  /**
   * Updates the firmware with the given identifier.
   *
   * @param id the identifier of the firmware to update.
   * @param firmware the new values of the firmware.
   * @return the updated firmware.
   */
  Firmware update(long id, Firmware firmware);

  /**
   * Finds all the firmwares.
   *
   * @return all the firmwares.
   */
  Firmwares findAll();

  /**
   * Finds all the firmwares, paginated.
   *
   * @param pageQuery the pagination query.
   * @return the corresponding page of firmwares.
   */
  PageResult<Firmware> findAll(PageQuery pageQuery);

  /**
   * Finds the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware.
   */
  Firmware findById(long id);
}
