package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.configuration.ConfigurationRepository;
import fr.uge.chargepointconfiguration.status.StatusRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * A ChargepointService doing database manipulations.
 */
@Service
public class ChargepointService {

  private final ChargepointRepository chargepointRepository;
  private final PageableChargepointRepository pageableChargepointRepository;

  private final ConfigurationRepository configurationRepository;

  private final StatusRepository statusRepository;

  /**
   * ChargepointService's constructor.
   *
   * @param chargepointRepository A ChargepointRepository accessing to database.
   * @param configurationRepository A ConfigurationRepository accessing to database.
   * @param statusRepository A StatusRepository accessing to database.
   */
  @Autowired
  public ChargepointService(ChargepointRepository chargepointRepository,
                            PageableChargepointRepository pageableChargepointRepository,
                            ConfigurationRepository configurationRepository,
                            StatusRepository statusRepository) {
    this.chargepointRepository = chargepointRepository;
    this.pageableChargepointRepository = pageableChargepointRepository;
    this.configurationRepository = configurationRepository;
    this.statusRepository = statusRepository;
  }

  /**
   * Create a chargepoint.
   *
   * @param createChargepointDto All the necessary information for a configuration creation.
   * @return A chargepoint created with its information.
   */
  public ChargepointDto save(CreateChargepointDto createChargepointDto) {
    var chargepoint = chargepointRepository.save(new Chargepoint(
        createChargepointDto.serialNumberChargepoint(),
        createChargepointDto.type(),
        createChargepointDto.constructor(),
        createChargepointDto.clientId(),
        createChargepointDto.serverAddress(),
        configurationRepository.findById(createChargepointDto.configuration()).orElseThrow(),
        statusRepository.findById(createChargepointDto.status()).orElseThrow()
    ));
    return chargepoint.toDto();
  }

  public List<ChargepointDto> getAllChargepoints() {
    return chargepointRepository.findAll().stream().map(Chargepoint::toDto).toList();
  }

  public Optional<ChargepointDto> getChargepointById(int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    return Optional.of(chargepointRepository.findById(id).orElseThrow().toDto());
  }

  /**
   * Search for chargepoints with a pagination.
   *
   * @param pageable The page requested
   * @param clientIdContains the pattern for ClientId
   * @return the list of corresponding chargepoint
   */
  public List<ChargepointDto> search(PageRequest pageable, String clientIdContains) {
    return pageableChargepointRepository.findAllByClientIdContaining(pageable, clientIdContains)
          .stream()
          .map(Chargepoint::toDto)
          .toList();
  }

}
