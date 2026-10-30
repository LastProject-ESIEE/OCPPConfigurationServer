package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.annotations.DomainService;
import fr.uge.ocpp.domain.exceptions.BadRequestException;
import fr.uge.ocpp.domain.exceptions.FirmwareAlreadyExistingException;
import fr.uge.ocpp.domain.exceptions.FirmwareNotFoundException;
import fr.uge.ocpp.domain.exceptions.TypeAllowedNotFoundException;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import fr.uge.ocpp.domain.ports.api.FirmwareService;
import fr.uge.ocpp.domain.ports.spi.FirmwareRepository;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepository;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Adapter implementing the firmware use cases on top of the SPI ports. */
@DomainService
public record FirmwareServiceAdapter(
    FirmwareRepository firmwareRepository, TypeAllowedRepository typeAllowedRepository)
    implements FirmwareService {

  /**
   * Creates the adapter.
   *
   * @param firmwareRepository the firmware persistence port.
   * @param typeAllowedRepository the allowed type persistence port.
   */
  public FirmwareServiceAdapter {
    Objects.requireNonNull(firmwareRepository);
    Objects.requireNonNull(typeAllowedRepository);
  }

  /**
   * Creates a firmware. The url must not be already used and at least one allowed type must be
   * provided and exist.
   *
   * @param firmware the firmware to create.
   * @return the created firmware.
   */
  @Override
  public Firmware save(Firmware firmware) {
    Objects.requireNonNull(firmware);
    checkFields(firmware);
    checkUrlNotUsed(firmware.url());
    var typesAllowed = resolveTypesAllowed(firmware.typesAllowed());
    var toSave =
        new Firmware(
            null, firmware.url(), firmware.version(), firmware.constructor(), typesAllowed);
    return firmwareRepository.save(toSave);
  }

  /**
   * Updates the firmware with the given identifier. The new url must not be already used by another
   * firmware and all the allowed types must exist.
   *
   * @param id the identifier of the firmware to update.
   * @param firmware the new values of the firmware.
   * @return the updated firmware.
   */
  @Override
  public Firmware update(long id, Firmware firmware) {
    Objects.requireNonNull(firmware);
    checkFields(firmware);
    firmwareRepository
        .findByUrl(firmware.url())
        .filter(existing -> existing.id() != id)
        .ifPresent(
            existing -> {
              throw new FirmwareAlreadyExistingException(
                  "Un firmware avec l'URL existe déjà : " + firmware.url());
            });
    var typesAllowed = resolveTypesAllowed(firmware.typesAllowed());
    var old =
        firmwareRepository
            .findById(id)
            .orElseThrow(() -> new FirmwareNotFoundException("Pas de firmware avec l'id : " + id));
    var toSave =
        old.withUrl(firmware.url())
            .withVersion(firmware.version())
            .withConstructor(firmware.constructor())
            .withTypesAllowed(typesAllowed);
    return firmwareRepository.save(toSave);
  }

  /**
   * Finds all the firmwares.
   *
   * @return all the firmwares.
   */
  @Override
  public Firmwares findAll() {
    return firmwareRepository.findAllByOrderByIdDesc();
  }

  /**
   * Finds the firmware with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return the corresponding firmware.
   */
  @Override
  public Firmware findById(long id) {
    return firmwareRepository
        .findById(id)
        .orElseThrow(() -> new FirmwareNotFoundException("Pas de firmware avec l'id : " + id));
  }

  private void checkFields(Firmware firmware) {
    if (firmware.version() == null
        || firmware.version().isBlank()
        || firmware.url() == null
        || firmware.url().isBlank()
        || firmware.constructor() == null
        || firmware.constructor().isBlank()) {
      throw new BadRequestException("Version, url, constructeur sont requis");
    }
  }

  private void checkUrlNotUsed(String url) {
    if (firmwareRepository.findByUrl(url).isPresent()) {
      throw new FirmwareAlreadyExistingException("Un firmware avec l'URL existe déjà : " + url);
    }
  }

  private TypesAllowed resolveTypesAllowed(TypesAllowed typesAllowed) {
    if (typesAllowed == null || typesAllowed.typesAllowedSet().isEmpty()) {
      throw new BadRequestException("Aucun modèle compatible n'est renseigné.");
    }
    var resolved = new LinkedHashSet<TypeAllowed>();
    for (var typeAllowed : typesAllowed.typesAllowedSet()) {
      resolved.add(
          typeAllowedRepository
              .findById(typeAllowed.id())
              .orElseThrow(
                  () ->
                      new TypeAllowedNotFoundException(
                          "Aucun modèle compatible avec l'id " + typeAllowed.id())));
    }
    return new TypesAllowed(Set.copyOf(resolved));
  }
}
