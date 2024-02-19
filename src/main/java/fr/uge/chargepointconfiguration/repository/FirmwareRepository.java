package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Firmware;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
  @Query("SELECT f FROM Firmware f WHERE f.version LIKE :version")
  Firmware findByVersion(@Param("version") String version);
}
