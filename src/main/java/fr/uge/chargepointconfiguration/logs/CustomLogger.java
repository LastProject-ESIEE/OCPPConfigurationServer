package fr.uge.chargepointconfiguration.logs;

import fr.uge.chargepointconfiguration.logs.sealed.Log;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A CustomLogger storing and displaying differents logs.
 */
@Component
public class CustomLogger {

  private final Logger logger = LogManager.getLogger("BRS-Configurator");

  private final CustomLoggerService customLoggerService;

  /**
   * CustomLogger's constructor.
   *
   * @param customLoggerService A CustomLogService.
   */
  @Autowired
  CustomLogger(CustomLoggerService customLoggerService) {
    this.customLoggerService = customLoggerService;
  }

  /**
   * Store a log in database and then display it.
   *
   * @param log The log going to be stored and displayed.
   */
  public void log(Log log) {
    var savedLog = customLoggerService.save(log);
    logger.log(Level.getLevel(savedLog.getLevel()), savedLog.text());
  }
}
