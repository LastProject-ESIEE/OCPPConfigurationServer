package fr.uge.ocpp.service.chargepoint;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The values required to create or update a charge point. */
public record CreateChargePointDto(@JsonProperty("serialNumber") String serialNumberChargepoint,
                                  String type,
                                  String constructor,
                                  String clientId,
                                  Long configuration) {

  /** The identifier meaning that the charge point has no configuration. */
  public static final long NO_CONFIG_ID = -1;
}
