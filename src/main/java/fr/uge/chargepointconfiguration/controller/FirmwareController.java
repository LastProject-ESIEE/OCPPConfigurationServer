package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.Firmware;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the firmware entity.
 */
@RestController
public class FirmwareController {

  private final FirmwareRepository firmwareRepository;

  /**
   * FirmwareController's constructor.
   *
   * @param firmwareRepository A FirmwareRepository.
   */
  @Autowired
  public FirmwareController(FirmwareRepository firmwareRepository) {
    this.firmwareRepository = firmwareRepository;
  }

  /**
   * Returns all the firmwares.
   *
   * @return A list of all the firmwares.
   */
  @GetMapping(value = "/firmwares")
  public List<Firmware> getAllChargepoints() {
    return firmwareRepository.findAll();
  }

  /**
   * Returns a firmware according to the given id.<br>
   * The optional will be empty if a firmware could not be found.
   *
   * @param id An int.
   * @return An optional of firmware.
   */
  @GetMapping(value = "/firmwares/{id}")
  public Optional<Firmware> getChargepointById(@PathVariable int id) {
    return firmwareRepository.findById(id);
  }
}
