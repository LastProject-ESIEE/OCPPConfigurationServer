package fr.uge.chargepointconfiguration.logs.technical;

import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import java.sql.Timestamp;

/**
 * DTO to read technical log in database.
 *
 * @param id Database id of the log stored.
 * @param date Date when the log had been created.
 * @param component {@link TechnicalLog.Component}
 * @param criticality {@link TechnicalLog.Criticality}
 * @param completeLog All the log in one String.
 */
public record TechnicalLogDto(
    int id,
    Timestamp date,
    TechnicalLog.Component component,
    TechnicalLog.Criticality criticality,
    String completeLog) {
}