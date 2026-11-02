package fr.uge.ocpp.websocket.ocpp;

import fr.uge.ocpp.websocket.ocpp.ocpp2.data.Component;
import java.util.Objects;

/**
 * Maps a configuration entry stored in the database (by its id) to the OCPP keys used by the charge
 * point.<br>
 * The OCPP 1.6 key depends on the charge point's firmware version (see {@link ConfigurationKey}),
 * the OCPP 2.0.1 key is given with its {@link Component}.
 */
public enum ConfigurationTranscriptor {
  LIGHT_INTENSITY(
      1,
      "Intensité de la LED",
      ConfigurationKey.LIGHT_INTENSITY,
      "LightIntensity",
      new Component("UNKNOWN"),
      "^(100|\\d{1,2})$"),
  NETWORK_PROFILE(
      2,
      "Adresse du prochain serveur",
      ConfigurationKey.NETWORK_PROFILE,
      "",
      new Component(""),
      ""),
  CHARGEPOINT_IDENTITY(
      3,
      "Nom de la borne",
      ConfigurationKey.CHARGEPOINT_IDENTITY,
      "Identity",
      new Component("SecurityCtrlr"),
      "^.{0,20}$"),
  LOCAL_AUTH_LIST(
      4,
      "Activer la liste des badges d'authentification ?",
      ConfigurationKey.LOCAL_AUTH_LIST,
      "LocalAuthListEnabled",
      new Component("LocalAuthListCtrlr"),
      ""),
  STATION_MAX_CURRENT(
      5,
      "Courant max de la borne",
      ConfigurationKey.STATION_MAX_CURRENT,
      "",
      new Component(""),
      ""),
  STATION_PASSWORD(
      6,
      "Changer le mot de passe de la borne",
      ConfigurationKey.STATION_PASSWORD,
      "BasicAuthPassword",
      new Component("SecurityCtrlr"),
      "^(?!.*[\\\\\",]).{10,40}$");

  private final int id;
  private final String fullName;
  private final ConfigurationKey ocpp16Key;
  private final String ocpp20Key;
  private final Component component;
  private final String regexRule;

  ConfigurationTranscriptor(
      int id,
      String fullName,
      ConfigurationKey ocpp16Key,
      String ocpp20Key,
      Component component,
      String regexRule) {
    this.id = id;
    this.fullName = Objects.requireNonNull(fullName);
    this.ocpp16Key = Objects.requireNonNull(ocpp16Key);
    this.ocpp20Key = Objects.requireNonNull(ocpp20Key);
    this.component = Objects.requireNonNull(component);
    this.regexRule = Objects.requireNonNull(regexRule);
  }

  /**
   * Returns the configuration entry's id in the database.
   *
   * @return the id.
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the configuration entry's display name.
   *
   * @return the full name.
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Returns the OCPP 1.6 key of this configuration entry.
   *
   * @return the {@link ConfigurationKey}.
   */
  public ConfigurationKey getOcpp16Key() {
    return ocpp16Key;
  }

  /**
   * Returns the OCPP 2.0.1 variable name of this configuration entry.
   *
   * @return the OCPP 2.0.1 key.
   */
  public String getOcpp20Key() {
    return ocpp20Key;
  }

  /**
   * Returns the OCPP 2.0.1 component of this configuration entry.
   *
   * @return the {@link Component}.
   */
  public Component getComponent() {
    return component;
  }

  /**
   * Returns the regex rule used to validate the value.
   *
   * @return the regex rule.
   */
  public String getRegexRule() {
    return regexRule;
  }

  /**
   * Returns the {@link ConfigurationTranscriptor} matching the given database id.
   *
   * @param id the configuration entry's id.
   * @return the matching enum value.
   */
  public static ConfigurationTranscriptor idToEnum(int id) {
    return switch (id) {
      case 1 -> LIGHT_INTENSITY;
      case 2 -> NETWORK_PROFILE;
      case 3 -> CHARGEPOINT_IDENTITY;
      case 4 -> LOCAL_AUTH_LIST;
      case 5 -> STATION_MAX_CURRENT;
      case 6 -> STATION_PASSWORD;
      default -> throw new IllegalStateException("Unknown key id: " + id);
    };
  }
}
