package fr.uge.chargepointconfiguration.logs.technical;

import fr.uge.chargepointconfiguration.logs.sealed.TechnicalLog;
import java.sql.Timestamp;
import org.apache.logging.log4j.Level;

/**
 * DTO to read technical log in database.
 *
 * @param id Database id of the log stored.
 * @param date Date when the log had been created.
 * @param component {@link TechnicalLog.Component}
 * @param level {@link Level}
 * @param completeLog All the log in one String.
 */
public record TechnicalLogDto(
    int id,
    Timestamp date,
    TechnicalLog.Component component,
    Level level,
    String completeLog) {
}