package fr.uge.chargepointconfiguration.firmware;

import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowedRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

  public Optional<Firmware> getFirmwareById(int id) {
    return firmwareRepository.findById(id);
  }

  public long countTotal() {
    return firmwareRepository.count();
  }

  /**
   * Search for {@link Firmware} with a pagination.
   *
   * @param pageable The page requested
   * @return the list of corresponding {@link Firmware}
   */
  public List<Firmware> getPage(PageRequest pageable) {
    return firmwareRepository.findAllByOrderByIdDesc(pageable)
            .stream().toList();
  }


  /**
   * Create a firmware.
   *
   * @param createFirmwareDto All the necessary information for a firmware creation.
   * @return A firmware created with its information.
   */
  public FirmwareDto save(CreateFirmwareDto createFirmwareDto) {
    var typesAllowed = new HashSet<TypeAllowed>();
    createFirmwareDto.typesAllowed().forEach(typeAllowedDto -> {
      typeAllowedRepository
              .findById(typeAllowedDto.id())
              .ifPresent(typesAllowed::add);
    });
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


  /**
   * Update a firmware.
   *
   * @param createFirmwareDto All the necessary information for a firmware update.
   * @return A firmware updated with its information.
   */
  public Optional<FirmwareDto> update(int id, CreateFirmwareDto createFirmwareDto) {
    var typesAllowed = new HashSet<TypeAllowed>();
    createFirmwareDto.typesAllowed().forEach(typeAllowedDto -> {
      typeAllowedRepository
              .findById(typeAllowedDto.id())
              .ifPresent(typesAllowed::add);
    });
    var currentConfiguration = firmwareRepository.findById(id);
    return currentConfiguration.map(firmware -> {
      firmware.setConstructor(createFirmwareDto.constructor());
      firmware.setVersion(createFirmwareDto.version());
      firmware.setUrl(createFirmwareDto.url());
      firmware.setTypesAllowed(typesAllowed);
      firmwareRepository.save(firmware);
      return firmware.toDto();
    });
  }
}
