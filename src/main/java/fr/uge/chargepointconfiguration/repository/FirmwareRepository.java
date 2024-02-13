package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Firmware;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the firmware.
 */
public interface FirmwareRepository extends CrudRepository<Firmware, Integer> {
}
