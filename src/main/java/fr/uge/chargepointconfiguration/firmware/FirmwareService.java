package fr.uge.chargepointconfiguration.firmware;

import fr.uge.chargepointconfiguration.chargepoint.CreateChargepointDto;
import fr.uge.chargepointconfiguration.errors.exceptions.BadRequestException;
import fr.uge.chargepointconfiguration.errors.exceptions.EntityAlreadyExistingException;
import fr.uge.chargepointconfiguration.errors.exceptions.EntityNotFoundException;
import fr.uge.chargepointconfiguration.shared.SearchUtils;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowedRepository;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * A FirmwareService doing database manipulations.
 */
@Service
public class FirmwareService {
  private final FirmwareRepository firmwareRepository;
  private final TypeAllowedRepository typeAllowedRepository;

  @Autowired
  public FirmwareService(
      FirmwareRepository firmwareRepository,
      TypeAllowedRepository typeAllowedRepository) {
    this.firmwareRepository = firmwareRepository;
    this.typeAllowedRepository = typeAllowedRepository;
  }

  public List<Firmware> getAllFirmwares() {
    return firmwareRepository.findAllByOrderByIdDesc();
  }

  public Firmware getFirmwareById(int id) {
    return firmwareRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Pas de firmware avec l'id : " + id));
  }

  /**
   * Count the number of entities with the constraint of the given request.
   *
   * @param request the request used to search
   * @return the amount of entities with the constraint of the given request
   */
  public long countTotalWithFilter(String request) {
    try {
      var condition = SearchUtils.computeSpecification(request, Firmware.class);
      return firmwareRepository.count(condition);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Requête invalide pour les filtres : " + request);
    }
  }

  /**
   * Search for {@link Firmware} with a pagination.
   *
   * @param request  the request used to search
   * @param pageable The page requested
   * @return the list of corresponding {@link Firmware}
   */
  public List<Firmware> search(String request, PageRequest pageable) {
    try {
      var condition = SearchUtils.computeSpecification(request, Firmware.class);
      return firmwareRepository.findAll(condition, pageable)
          .stream().toList();
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Requête invalide pour les filtres : " + request);
    }
  }


  /**
   * Create a firmware.
   *
   * @param createFirmwareDto All the necessary information for a firmware creation.
   * @return A firmware created with its information.
   */
  public FirmwareDto save(CreateFirmwareDto createFirmwareDto) {
    checkAlreadyExisting(createFirmwareDto);
    checkFieldsFirmware(createFirmwareDto);

    if (createFirmwareDto.typesAllowed().isEmpty()) {
      throw new BadRequestException("Aucun modèle compatible n'est renseigné.");
    }
    var typesAllowed = new HashSet<TypeAllowed>();
    createFirmwareDto.typesAllowed().forEach(typeAllowedDto -> typeAllowedRepository
        .findById(typeAllowedDto.id())
        .ifPresentOrElse(typesAllowed::add, () -> {
          throw new EntityNotFoundException("Aucun modèle compatible avec l'id "
                                            + typeAllowedDto.id());
        }));
    var firmware = firmwareRepository.save(
        new Firmware(
            createFirmwareDto.url(),
            createFirmwareDto.version(),
            createFirmwareDto.constructor(),
            typesAllowed
        )
    );
    return firmware.toDto();
  }

  private void checkAlreadyExisting(CreateFirmwareDto createFirmwareDto) {
    if (firmwareRepository.findByUrl(createFirmwareDto.url()).isPresent()) {
      throw new EntityAlreadyExistingException(
          "Un firmware avec l'URL existe déjà : " + createFirmwareDto.url());
    }
  }


  /**
   * Update a firmware.
   *
   * @param createFirmwareDto All the necessary information for a firmware update.
   * @return A firmware updated with its information.
   */
  public Firmware update(int id, CreateFirmwareDto createFirmwareDto) {
    checkAlreadyExisting(createFirmwareDto);

    var typesAllowed = new HashSet<TypeAllowed>();
    createFirmwareDto.typesAllowed().forEach(typeAllowedDto -> typeAllowedRepository
        .findById(typeAllowedDto.id())
        .ifPresentOrElse(typesAllowed::add, () -> {
          throw new EntityNotFoundException(
              "Aucun modèle compatible avec l'id " + typeAllowedDto.id());
        }));

    var firmware = firmwareRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Aucun firmware avec l'id " + id));

    firmware.setConstructor(createFirmwareDto.constructor());
    firmware.setVersion(createFirmwareDto.version());
    firmware.setUrl(createFirmwareDto.url());
    firmware.setTypesAllowed(typesAllowed);
    return firmwareRepository.save(firmware);
  }

  private static void checkFieldsFirmware(CreateFirmwareDto newValues) {
    if (newValues.version().isBlank()
        || newValues.url().isBlank()
        || newValues.constructor().isBlank()) {
      throw new BadRequestException("Version, url, constructeur sont requis");
    }
  }
}
