package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
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


  /**
   * Return a list of registered Chargepoints from database.
   *
   * @return A list of Chargepoints or an empty list if no chargepoints are registered.
   */
  List<Chargepoint> findAll();
}
