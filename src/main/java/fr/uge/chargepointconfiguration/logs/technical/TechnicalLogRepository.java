package fr.uge.chargepointconfiguration.logs.technical;

import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import java.util.List;
import org.apache.logging.log4j.Level;
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
   * @param level {@link Level}
   * @return the list of technical logs by component.
   */
  List<TechnicalLog> findAllByComponentAndLevel(
          TechnicalLog.Component component,
          Level level);

  /**
   * Method to return all technical logs by the criticality.
   *
   * @param level {@link Level}
   * @return the list of technical logs by criticality.
   */
  List<TechnicalLog> findAllByLevel(Level level);
}
