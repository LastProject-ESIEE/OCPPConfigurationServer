package fr.uge.ocpp.service.firmware;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.ports.api.FirmwareService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The REST endpoints of the firmware API. */
@RestController
@RequestMapping("/api/firmware")
public class FirmwareController {

  private final FirmwareService firmwareService;

  /**
   * Creates the controller.
   *
   * @param firmwareService the firmware use cases.
   */
  public FirmwareController(FirmwareService firmwareService) {
    this.firmwareService = firmwareService;
  }

  /**
   * Returns all the firmwares.
   *
   * @return all the firmwares.
   */
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('VISUALIZER')")
  public List<FirmwareDto> getAllFirmwares() {
    return firmwareService.findAll().firmwareList().stream().map(FirmwareDto::of).toList();
  }

  /**
   * Returns the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware.
   */
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VISUALIZER')")
  public FirmwareDto getFirmwareById(@PathVariable long id) {
    return FirmwareDto.of(firmwareService.findById(id));
  }

  /**
   * Creates a firmware.
   *
   * @param createFirmwareDto the values of the firmware.
   * @return the created firmware.
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<FirmwareDto> create(@RequestBody CreateFirmwareDto createFirmwareDto) {
    Firmware firmware = firmwareService.save(FirmwareMapper.toFirmware(null, createFirmwareDto));
    return new ResponseEntity<>(FirmwareDto.of(firmware), HttpStatus.CREATED);
  }

  /**
   * Updates the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @param createFirmwareDto the new values of the firmware.
   * @return the updated firmware.
   */
  @PatchMapping("/update/{id}")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<FirmwareDto> update(
      @PathVariable long id, @RequestBody CreateFirmwareDto createFirmwareDto) {
    Firmware firmware =
        firmwareService.update(id, FirmwareMapper.toFirmware(id, createFirmwareDto));
    return new ResponseEntity<>(FirmwareDto.of(firmware), HttpStatus.OK);
  }
}
