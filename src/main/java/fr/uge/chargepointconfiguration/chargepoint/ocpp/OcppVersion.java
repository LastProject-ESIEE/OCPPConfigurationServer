package fr.uge.chargepointconfiguration.chargepoint.ocpp;

import java.util.Optional;

/**
 * An enum to define the multiple version of the OCPP protocol.
 */
public enum OcppVersion {
  V1_6,
  V2;

  /**
   * Returns the correct version according to the given string.<br>
   * It will return an empty optional if the version could not be found.
   *
   * @param version A String.
   * @return An optional of the OCPP version.
   */
  public static Optional<OcppVersion> parse(String version) {
    return switch (version) {
      case "ocpp1.6" -> Optional.of(V1_6);
      case "ocpp2.0.1" -> Optional.of(V2);
      default -> Optional.empty();
    };
  }
}
