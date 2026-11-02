package fr.uge.ocpp.websocket.ocpp;

import java.util.Objects;

/**
 * Represents an OCPP configuration key.<br>
 * The key name sent to the charge point depends on the charge point's firmware major version (Alfen
 * firmware 4.x, 5.x and 6.x do not use the same key names).
 */
public enum ConfigurationKey {
  LIGHT_INTENSITY("LightIntensity", "LightIntensity", "LightIntensity"),
  NETWORK_PROFILE("BackOffice-URL-wired", "BackOffice-URL-wired", "BackOfficeNetworkProfile1"),
  CHARGEPOINT_IDENTITY("Identity", "Identity", "Identity"),
  LOCAL_AUTH_LIST("LocalAuthListEnabled", "LocalAuthListEnabled", "LocalAuthListEnabled"),
  STATION_MAX_CURRENT("Station-MaxCurrent", "Station-MaxCurrent", "Station-MaxCurrent"),
  STATION_PASSWORD("PW-SetChargerPassword", "PW-SetChargerPassword", "PW-SetChargerPassword");

  private final String firmwareV4KeyName;
  private final String firmwareV5KeyName;
  private final String firmwareV6KeyName;

  ConfigurationKey(String firmwareV4KeyName, String firmwareV5KeyName, String firmwareV6KeyName) {
    this.firmwareV4KeyName = Objects.requireNonNull(firmwareV4KeyName);
    this.firmwareV5KeyName = Objects.requireNonNull(firmwareV5KeyName);
    this.firmwareV6KeyName = Objects.requireNonNull(firmwareV6KeyName);
  }

  /**
   * Returns the OCPP 1.6 key name according to the charge point's firmware version.
   *
   * @param firmwareVersion the charge point's firmware version (e.g. "6.1.0").
   * @return the key name to use in a ChangeConfiguration request.
   */
  public String getKeyAccordingToVersion(String firmwareVersion) {
    var majorVersion = firmwareVersion.split("\\.")[0];
    return switch (majorVersion) {
      case "4" -> firmwareV4KeyName;
      case "5" -> firmwareV5KeyName;
      case "6" -> firmwareV6KeyName;
      default -> throw new IllegalStateException("Unknown firmware version: " + firmwareVersion);
    };
  }

  /**
   * Returns the value format according to the charge point's firmware version.<br>
   * Firmware 6.x expects the network profile as a composite string.
   *
   * @param firmwareVersion the charge point's firmware version (e.g. "6.1.0").
   * @return the format string to apply to the value.
   */
  public String getValueFormatAccordingToVersion(String firmwareVersion) {
    var majorVersion = firmwareVersion.split("\\.")[0];
    return switch (majorVersion) {
      case "4", "5" -> "%s";
      case "6" ->
          "ocppVersion{OCPP16}ocppCsmsUrl{%s}"
              + "messageTimeout{10}securityProfile{0}ocppInterface{Wired0}";
      default -> throw new IllegalStateException("Unknown firmware version: " + firmwareVersion);
    };
  }
}
