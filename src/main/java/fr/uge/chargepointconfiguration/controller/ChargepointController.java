package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.Chargepoint;
import fr.uge.chargepointconfiguration.repository.ChargepointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class ChargepointController {

  private final ChargepointRepository chargepointRepository;

  @Autowired
  public ChargepointController(ChargepointRepository chargepointRepository) {
    this.chargepointRepository = chargepointRepository;
  }

  @GetMapping(value = "/chargepoints")
  public List<Chargepoint> getAllChargepoints(){
//    ArrayList<Chargepoint> chargepoints = new ArrayList<>();
//    chargepointRepository.findAll().forEach(chargepoints::add);
//    return chargepoints;
    return StreamSupport.stream(chargepointRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  @GetMapping(value = "/chargepoints/{id}")
  public Optional<Chargepoint> getChargepointById(@PathVariable int id) {
    return chargepointRepository.findById(id);
  }
}
