package fr.uge.chargepointconfiguration.logs.sealed;

import org.apache.logging.log4j.Level;

/**
 * Interface used to define an entity log.
 */
public sealed interface LogEntity permits BusinessLogEntity, TechnicalLogEntity {

  /**
   * Get the level as interface type.
   *
   * @return String version of {@link Level}
   */
  String getLevel();

  /**
   * Display all the infos of a log.
   *
   * @return String containing of the datas from a log.
   */
  String text();
}
