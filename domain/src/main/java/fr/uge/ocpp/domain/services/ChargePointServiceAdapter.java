package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.annotations.DomainService;
import fr.uge.ocpp.domain.exceptions.ChargePointNotFoundException;
import fr.uge.ocpp.domain.exceptions.ConfigurationNotFoundException;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;

import java.util.Objects;

@DomainService
public record ChargePointServiceAdapter(ChargePointRepository chargePointRepository,
                                        ConfigurationRepository configurationRepository) implements ChargePointService {

  public ChargePointServiceAdapter {
    Objects.requireNonNull(chargePointRepository);
    Objects.requireNonNull(configurationRepository);
  }

  @Override
  public ChargePoint save(ChargePoint chargePoint) {
    Configuration configuration = configurationRepository.findById(chargePoint.id()).orElseThrow(() -> new ConfigurationNotFoundException("Configuration not found"));
    ChargePoint toSave = ChargePoint.of(chargePoint.serialNumber(), chargePoint.type(), chargePoint.constructor(), configuration);

    return chargePointRepository.save(toSave);
  }

  @Override
  public ChargePoints findAll() {
    return chargePointRepository.findAllByOrderByIdDesc();
  }

  @Override
  public ChargePoint findById(long id) {
    return chargePointRepository.findById(id).orElseThrow(() -> new ChargePointNotFoundException("Charge point not found"));
  }

  @Override
  public ChargePoints findAll(String request, PageQuery pageQuery) {
    return null;
  }

  @Override
  public long countTotalWithFilter(String request) {
    return 0;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public ChargePoint update(int id, ChargePoint chargePoint) {
    return null;
  }
}
