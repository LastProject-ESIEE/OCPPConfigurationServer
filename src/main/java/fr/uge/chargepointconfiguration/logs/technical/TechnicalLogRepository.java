package fr.uge.chargepointconfiguration.logs.technical;

import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for technical log.
 */
@Repository
public interface TechnicalLogRepository extends CrudRepository<TechnicalLog, Integer> {

  /**
   * Method to return all technical logs.
   *
   * @return a list of all technical logs.
   */
  List<TechnicalLog> findAll();

  /**
   * Method to return all technical logs by the component.
   *
   * @param component the type of component in the system.
   * @param criticality the critical level of the technical log.
   * @return the list of technical logs by component.
   */
  List<TechnicalLog> findAllByComponentAndCriticality(
          TechnicalLog.Component component,
          TechnicalLog.Criticality criticality);

  /**
   * Method to return all technical logs by the criticality.
   *
   * @param criticality the critical level of the technical log.
   * @return the list of technical logs by criticality.
   */
  List<TechnicalLog> findAllByCriticality(TechnicalLog.Criticality criticality);
}
