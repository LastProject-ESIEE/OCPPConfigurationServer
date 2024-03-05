package fr.uge.chargepointconfiguration.firmware;

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

  @Autowired
  public FirmwareService(FirmwareRepository firmwareRepository) {
    this.firmwareRepository = firmwareRepository;
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
   * Create a configuration.
   *
   * @param createFirmwareDto All the necessary information for a firmware creation.
   * @return A firmware created with its information.
   */
  public FirmwareDto save(CreateFirmwareDto createFirmwareDto) {
    var configuration = firmwareRepository.save(
            new Firmware(
                    createFirmwareDto.url(),
                    createFirmwareDto.version(),
                    createFirmwareDto.constructor(),
                    new HashSet<>()
            )
    );
    return configuration.toDto();
  }
}
