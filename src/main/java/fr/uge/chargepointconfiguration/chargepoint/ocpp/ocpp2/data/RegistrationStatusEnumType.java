package fr.uge.chargepointconfiguration.chargepoint.ocpp.ocpp2.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.uge.chargepointconfiguration.chargepoint.ocpp.RegistrationStatus;

/**
 * For the OCPP 2.0 BootNotificationResponse.
 *
 * @param status The status represented by a custom enum.
 */
public record RegistrationStatusEnumType(@JsonProperty("enum") RegistrationStatus status) {
}
