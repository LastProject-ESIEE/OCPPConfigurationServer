package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.annotations.DomainService;
import fr.uge.ocpp.domain.exceptions.BadRequestException;
import fr.uge.ocpp.domain.exceptions.ChargePointAlreadyExistingException;
import fr.uge.ocpp.domain.exceptions.ChargePointNotFoundException;
import fr.uge.ocpp.domain.exceptions.ConfigurationNotFoundException;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
import fr.uge.ocpp.domain.ports.spi.ChargePointRepository;
import fr.uge.ocpp.domain.ports.spi.ConfigurationRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

/** Adapter implementing the charge point use cases on top of the SPI ports. */
@DomainService
public record ChargePointServiceAdapter(
    ChargePointRepository chargePointRepository,
    ConfigurationRepository configurationRepository,
    Clock clock)
    implements ChargePointService {

  /**
   * Creates the adapter.
   *
   * @param chargePointRepository the charge point persistence port.
   * @param configurationRepository the configuration persistence port.
   * @param clock the clock used to timestamp updates.
   */
  public ChargePointServiceAdapter {
    Objects.requireNonNull(chargePointRepository);
    Objects.requireNonNull(configurationRepository);
    Objects.requireNonNull(clock);
  }

  /**
   * Creates a charge point. A charge point with the same serial number and constructor must not
   * already exist.
   *
   * @param chargePoint the charge point to create.
   * @return the created charge point.
   */
  @Override
  public ChargePoint save(ChargePoint chargePoint) {
    Objects.requireNonNull(chargePoint);
    checkFields(chargePoint);
    checkAlreadyExisting(chargePoint.serialNumber(), chargePoint.constructor());
    var configuration = resolveConfiguration(chargePoint.configuration());
    var toSave =
        ChargePoint.of(
            chargePoint.serialNumber(),
            chargePoint.type(),
            chargePoint.constructor(),
            chargePoint.clientId(),
            configuration);
    return chargePointRepository.save(toSave);
  }

  /**
   * Finds all the charge points.
   *
   * @return all the charge points.
   */
  @Override
  public ChargePoints findAll() {
    return chargePointRepository.findAllByOrderByIdDesc();
  }

  /**
   * Finds the charge points matching the given request, paginated.
   *
   * @param request the search request.
   * @param pageQuery the pagination query.
   * @return the corresponding charge points.
   */
  @Override
  public ChargePoints findAll(String request, PageQuery pageQuery) {
    // TODO: add query and request
    return chargePointRepository.findAllByOrderByIdDesc();
  }

  /**
   * Finds the charge point with the given identifier.
   *
   * @param id the identifier of the charge point.
   * @return the corresponding charge point.
   */
  @Override
  public ChargePoint findById(long id) {
    return chargePointRepository
        .findById(id)
        .orElseThrow(() -> new ChargePointNotFoundException("Pas de borne avec l'id : " + id));
  }

  /**
   * Counts the charge points matching the given request.
   *
   * @param request the search request.
   * @return the number of matching charge points.
   */
  @Override
  public long countTotalWithFilter(String request) {
    return 0;
  }

  /**
   * Counts all the charge points.
   *
   * @return the number of charge points.
   */
  @Override
  public long count() {
    return 0;
  }

  /**
   * Updates the charge point with the given identifier. The new serial number and constructor must
   * not be already used by another charge point.
   *
   * @param id the identifier of the charge point to update.
   * @param newValues the new values of the charge point.
   * @return the updated charge point.
   */
  @Override
  public ChargePoint update(long id, ChargePoint newValues) {
    Objects.requireNonNull(newValues);
    checkFields(newValues);
    var old =
        chargePointRepository
            .findById(id)
            .orElseThrow(() -> new ChargePointNotFoundException("Pas de borne avec l'id : " + id));
    chargePointRepository
        .findBySerialNumberChargepointAndConstructor(
            newValues.serialNumber(), newValues.constructor())
        .filter(existing -> existing.id() != id)
        .ifPresent(
            existing -> {
              throw new ChargePointAlreadyExistingException(
                  "Une borne utilise déjà ce numéro de série et constructeur : "
                      + newValues.serialNumber()
                      + ", "
                      + newValues.constructor());
            });
    var configuration = resolveConfiguration(newValues.configuration());
    var toSave =
        old.withSerialNumber(newValues.serialNumber())
            .withType(newValues.type())
            .withConstructor(newValues.constructor())
            .withClientId(newValues.clientId())
            .withConfiguration(configuration)
            .withLastUpdate(LocalDateTime.now(clock));
    return chargePointRepository.save(toSave);
  }

  private void checkFields(ChargePoint chargePoint) {
    if (chargePoint.serialNumber() == null
        || chargePoint.serialNumber().isBlank()
        || chargePoint.constructor() == null
        || chargePoint.constructor().isBlank()
        || chargePoint.type() == null
        || chargePoint.type().isBlank()
        || chargePoint.clientId() == null
        || chargePoint.clientId().isBlank()) {
      throw new BadRequestException(
          "Constructeur, numéro de série, type et identifiant client sont requis");
    }
  }

  private void checkAlreadyExisting(String serialNumber, String constructor) {
    if (chargePointRepository
        .findBySerialNumberChargepointAndConstructor(serialNumber, constructor)
        .isPresent()) {
      throw new ChargePointAlreadyExistingException(
          "Une borne utilise déjà ce numéro de série et constructeur : "
              + serialNumber
              + ", "
              + constructor);
    }
  }

  private Configuration resolveConfiguration(Configuration configuration) {
    if (configuration == null) {
      return null;
    }
    return configurationRepository
        .findById(configuration.id())
        .orElseThrow(
            () ->
                new ConfigurationNotFoundException(
                    "Pas de configuration avec l'id : " + configuration.id()));
  }
}
