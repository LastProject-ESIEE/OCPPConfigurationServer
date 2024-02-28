package fr.uge.chargepointconfiguration.firmware;

import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * A FirmwareService doing database manipulations.
 */
@Service
public class FirmwareService {
  private final FirmwareRepository firmwareRepository;

  @Autowired
  public FirmwareService(FirmwareRepository firmwareRepository) {
    this.firmwareRepository = firmwareRepository;
  }

  public List<Firmware> getAllFirmwares() {
    return firmwareRepository.findAll();
  }

  public Optional<Firmware> getFirmwareById(int id) {
    return firmwareRepository.findById(id);
  }

  public long countTotal() {
    return firmwareRepository.count();
  }

  /**
   * Search for {@link FirmwareDto} with a pagination.
   *
   * @param pageable The page requested
   * @return the list of corresponding {@link FirmwareDto}
   */
  public List<FirmwareDto> getPage(PageRequest pageable) {
    return firmwareRepository.findAll(pageable)
          .stream()
          .map(entity -> new FirmwareDto(entity.getId(),
                entity.getUrl(),
                entity.getVersion(),
                entity.getConstructor(),
                entity.getTypesAllowed().stream().map(TypeAllowed::toDto)
                      .collect(Collectors.toSet())
          ))
          .toList();
  }
}
