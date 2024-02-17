package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Chargepoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for the charge point.
 */
@Repository
public interface ChargepointRepository extends CrudRepository<Chargepoint, Integer> {

  /**
   * Returns a Chargepoint from the database according to the serial number.
   *
   * @param serialNumber Chargepoint's unique serial number.
   * @return The correct Chargepoint or null if the chargepoint couldn't be found.
   */
  @Query("SELECT c FROM Chargepoint c WHERE c.serialNumberChargepoint = :serialNumber")
  Chargepoint findBySerialNumber(@Param("serialNumber") String serialNumber);
}
