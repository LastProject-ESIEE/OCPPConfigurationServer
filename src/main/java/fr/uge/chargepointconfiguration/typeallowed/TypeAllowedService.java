package fr.uge.chargepointconfiguration.typeallowed;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointDto;
import fr.uge.chargepointconfiguration.configuration.Configuration;
import fr.uge.chargepointconfiguration.configuration.ConfigurationDto;
import fr.uge.chargepointconfiguration.configuration.ConfigurationRepository;
import fr.uge.chargepointconfiguration.configuration.CreateConfigurationDto;
import fr.uge.chargepointconfiguration.firmware.FirmwareRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TypeAllowedService manage database manipulations.
 */
@Service
public class TypeAllowedService {

  private final TypeAllowedRepository typeAllowedRepository;

  /**
   * TypeAllowedService's constructor.
   *
   * @param typeAllowedRepository A ConfigurationRepository accessing to database.
   */
  @Autowired
  public TypeAllowedService(TypeAllowedRepository typeAllowedRepository) {
    this.typeAllowedRepository = typeAllowedRepository;
  }


  /**
   * Get all type allowed entries from the database.
   *
   * @return The list of all type allowed.
   */
  public List<TypeAllowed> getAll() {
    return typeAllowedRepository.findAll();
  }

}
