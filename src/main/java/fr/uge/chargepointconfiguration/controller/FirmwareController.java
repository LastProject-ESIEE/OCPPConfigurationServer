package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.Firmware;
import fr.uge.chargepointconfiguration.repository.FirmwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class FirmwareController {
  private final FirmwareRepository firmwareRepository;

  @Autowired
  public FirmwareController(FirmwareRepository firmwareRepository) {
    this.firmwareRepository = firmwareRepository;
  }

  @GetMapping(value = "/firmwares")
  public List<Firmware> getAllChargepoints(){
//    ArrayList<Firmware> firmwares = new ArrayList<>();
//    firmwareRepository.findAll().forEach(firmwares::add);
//    return firmwares;
    return StreamSupport.stream(firmwareRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  @GetMapping(value = "/firmwares/{id}")
  public Optional<Firmware> getChargepointById(@PathVariable int id) {
    return firmwareRepository.findById(id);
  }
}
