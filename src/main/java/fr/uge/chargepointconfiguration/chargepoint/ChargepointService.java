package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.configuration.Configuration;
import fr.uge.chargepointconfiguration.configuration.ConfigurationRepository;
import fr.uge.chargepointconfiguration.status.Status;
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

  private final ConfigurationRepository configurationRepository;

  private final StatusRepository statusRepository;

  /**
   * ChargepointService's constructor.
   *
   * @param chargepointRepository   A ChargepointRepository accessing to database.
   * @param configurationRepository A ConfigurationRepository accessing to database.
   * @param statusRepository        A StatusRepository accessing to database.
   */
  @Autowired
  public ChargepointService(ChargepointRepository chargepointRepository,
                            ConfigurationRepository configurationRepository,
                            StatusRepository statusRepository) {
    this.chargepointRepository = chargepointRepository;
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
    var status = statusRepository.save(new Status());
    var configuration = configurationRepository.findById(createChargepointDto.configuration())
        .orElseThrow();
    var chargepoint = chargepointRepository.save(new Chargepoint(
        createChargepointDto.serialNumberChargepoint(),
        createChargepointDto.type(),
        createChargepointDto.constructor(),
        createChargepointDto.clientId(),
        "192.168.0.5",  // TODO variable environnement
        configuration,
        status
    ));
    return chargepoint.toDto();
  }

  public List<ChargepointDto> getAllChargepoints() {
    return chargepointRepository.findAllByOrderByIdDesc().stream().map(Chargepoint::toDto).toList();
  }

  public Optional<ChargepointDto> getChargepointById(int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    return Optional.of(chargepointRepository.findById(id).orElseThrow().toDto());
  }

  /**
   * Search for chargepoints with a pagination.
   *
   * @param pageable         The page requested
   * @param clientIdContains the pattern for ClientId
   * @return the list of corresponding chargepoint
   */
  public List<Chargepoint> search(PageRequest pageable, String clientIdContains) {
    return chargepointRepository
        .findAllByClientIdContainingIgnoreCaseOrderByIdDesc(pageable, clientIdContains)
        .stream().toList();
  }

  public long countTotal() {
    return chargepointRepository.count();
  }

  /**
   * Update a chargepoint and returns the updated chargepoint.
   *
   * @param id id of the chargepoint to update
   * @param newValues the new values chargepoint
   * @return the updated chargepoint
   */
  public Chargepoint update(int id, CreateChargepointDto newValues) {

    var chargepoint = chargepointRepository.findById(id).orElseThrow();
    chargepoint.setSerialNumberChargepoint(newValues.serialNumberChargepoint());
    chargepoint.setClientId(newValues.clientId());
    chargepoint.setConstructor(newValues.constructor());
    chargepoint.setType(newValues.type());

    if (newValues.configuration() == Configuration.NO_CONFIG_ID) {
      chargepoint.setConfiguration(null);
    } else {
      chargepoint.setConfiguration(
            configurationRepository.findById(newValues.configuration()).orElseThrow()
      );
    }

    return chargepointRepository.save(chargepoint);
  }
}
