package fr.uge.chargepointconfiguration.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A StatusService doing database manipulations.
 */
@Service
public class StatusService {

  private final StatusRepository statusRepository;

  /**
   * StatusService's constructor.
   *
   * @param statusRepository A StatusRepository accessing to database.
   */
  @Autowired
  public StatusService(StatusRepository statusRepository) {
    this.statusRepository = statusRepository;
  }

  /**
   * Init a status.
   *
   * @return A new status with default values.
   */
  public StatusDto save() {
    var status = statusRepository.save(new Status());
    return status.toDto();
  }
}
