package fr.uge.chargepointconfiguration.status;

import java.sql.Timestamp;

/**
 * DTO to read status in database.
 *
 * @param id Database id of the status stored.
 * @param lastUpdate Last time this chargepoint status has been changed.
 * @param error Show the last chargepoint error's log.
 * @param state Represent the state of connection of the chargepoint.
 * @param step The mods which a machine can be.
 * @param status The status of the process.<br>
 */
public record StatusDto(
    int id,
    Timestamp lastUpdate,
    String error,
    boolean state,
    Status.Step step,
    Status.StatusProcess status) {
}
