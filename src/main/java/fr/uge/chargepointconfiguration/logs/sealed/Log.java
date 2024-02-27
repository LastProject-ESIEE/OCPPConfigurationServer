package fr.uge.chargepointconfiguration.logs.sealed;

/**
 * Interface used to define a log.
 */
public sealed interface Log permits BusinessLog, TechnicalLog {

  String getLevel();

  String text();
}
