package fr.uge.ocpp.service.chargepoint;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
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

/** The REST endpoints of the charge point API. */
@RestController
@RequestMapping("/api/chargepoint")
public class ChargePointController {

  private final ChargePointService chargePointService;

  /**
   * Creates the controller.
   *
   * @param chargePointService the charge point use cases.
   */
  public ChargePointController(ChargePointService chargePointService) {
    this.chargePointService = chargePointService;
  }

  /**
   * Returns all the charge points.
   *
   * @return all the charge points.
   */
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('VISUALIZER')")
  public List<ChargePointDto> getAllChargepoints() {
    return chargePointService.findAll().chargePointList().stream().map(ChargePointDto::of).toList();
  }

  /**
   * Returns the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @return the corresponding charge point.
   */
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasRole('VISUALIZER')")
  public ChargePointDto getChargepointById(@PathVariable long id) {
    return ChargePointDto.of(chargePointService.findById(id));
  }

  /**
   * Creates a charge point.
   *
   * @param createChargePointDto the values of the charge point.
   * @return the created charge point.
   */
  @PostMapping("/create")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ChargePointDto> create(
      @RequestBody CreateChargePointDto createChargePointDto) {
    ChargePoint chargePoint =
        chargePointService.save(ChargePointMapper.toChargePoint(createChargePointDto));
    return new ResponseEntity<>(ChargePointDto.of(chargePoint), HttpStatus.CREATED);
  }

  /**
   * Updates the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @param createChargePointDto the new values of the charge point.
   * @return the updated charge point.
   */
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ChargePointDto> update(
      @PathVariable long id, @RequestBody CreateChargePointDto createChargePointDto) {
    ChargePoint chargePoint =
        chargePointService.update(id, ChargePointMapper.toChargePoint(createChargePointDto));
    return new ResponseEntity<>(ChargePointDto.of(chargePoint), HttpStatus.OK);
  }
}
