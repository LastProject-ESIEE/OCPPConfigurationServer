package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Firmware;
import org.springframework.data.repository.CrudRepository;

public interface FirmwareRepository extends CrudRepository<Firmware, Integer> {
}
