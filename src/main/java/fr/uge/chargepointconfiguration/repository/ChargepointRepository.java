package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.Chargepoint;
import io.micrometer.common.lang.NonNullApi;
import java.util.List;
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
   * Returns a Chargepoint from the database according to the serial number and vendor.
   *
   * @param serialNumber Chargepoint's unique serial number.
   * @param constructor Chargepoint's vendor.
   * @return The correct Chargepoint or null if the chargepoint couldn't be found.
   */
  Chargepoint findBySerialNumberChargepointAndConstructor(String serialNumber,
                                                          String constructor);

  List<Chargepoint> findAll();
}
