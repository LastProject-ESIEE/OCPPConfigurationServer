package fr.uge.chargepointconfiguration.firmware;

import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the firmware.
 */
public interface FirmwareRepository extends CrudRepository<Firmware, Integer> {

  /**
   * Returns a Firmware from the database according to the version.
   *
   * @param version Firmware's unique version.
   * @return The correct Firmware or null if the firmware couldn't be found.
   */
  Firmware findByVersion(String version);

  /**
   * Returns all the firmwares from the database according to the compatibility table.
   *
   * @param typeAllowed {@link TypeAllowed} which is the compatibility for a firmware.
   * @return {@link Firmware}.
   */
  List<Firmware> findAllByTypesAllowedContainsOrderByVersionAsc(TypeAllowed typeAllowed);

  /**
   * Return a list of registered Firmwares from database.
   *
   * @return A list of Firmwares or an empty list if no firmwares are registered.
   */
  List<Firmware> findAll();
}
