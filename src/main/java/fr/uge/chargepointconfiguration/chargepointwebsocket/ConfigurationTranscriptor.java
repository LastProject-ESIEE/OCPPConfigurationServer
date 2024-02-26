package fr.uge.chargepointconfiguration.chargepointwebsocket;

import fr.uge.chargepointconfiguration.chargepointwebsocket.ocpp.ocpp2.data.Component;
import java.util.Objects;

/**
 * For a full name, we give the correct OCPP 1.6 and OCPP 2.0.1 key (and component).<br>
 * It is used for translating the given configuration file from the database and for the front.
 */
public enum ConfigurationTranscriptor {

  LIGHT_INTENSITY("Intensité de la LED",
          "LightIntensity",
          "UNKNOWN",
          new Component("UNKNOWN"),
          "^(100|\\d{1,2})$"),

  NETWORK_PROFILE("Adresse IP/DNS du point de charge",
          "BackOfficeNetworkProfile1",
          "",
          new Component(""),
          ""),

  VENDOR_NAME("Nom du constructeur",
          "Identity",
          "Identity",
          new Component("SecurityCtrlr"),
          "^.{0,20}$"),

  LOCAL_AUTH_LIST("Liste d'authentification local activé ?",
          "LocalAuthListEnabled",
          "LocalAuthListEnabled",
          new Component("LocalAuthListCtrlr"),
          ""),

  STATION_MAX_CURRENT("Courant max du point de charge",
          "Station-MaxCurrent",
          "",
          new Component(""),
          ""),

  STATION_PASSWORD("Changer le mot de passe du point de charge",
          "PW-SetChargerPassword",
          "BasicAuthPassword",
          new Component("SecurityCtrlr"),
          "^(?!.*[\\\\\",]).{10,40}$");

  private final String fullName;
  private final String ocpp16Key;
  private final String ocpp20Key;
  private final Component component;
  private final String regexRule;

  /**
   * Enum's constructor. Should be private and not called outside this enum.
   *
   * @param fullName The name which will be displayed to the user.
   * @param ocpp16Key The configuration key according to the OCPP 1.6 protocol.
   * @param ocpp20Key The configuration key according to the OCPP 2.0.1 protocol.
   * @param component {@link Component}.
   * @param regexRule The rule which a user should respect when giving a value to the given key.
   */
  ConfigurationTranscriptor(String fullName,
                            String ocpp16Key,
                            String ocpp20Key,
                            Component component,
                            String regexRule) {
    this.fullName = Objects.requireNonNull(fullName);
    this.ocpp16Key = Objects.requireNonNull(ocpp16Key);
    this.ocpp20Key = Objects.requireNonNull(ocpp20Key);
    this.component = Objects.requireNonNull(component);
    this.regexRule = Objects.requireNonNull(regexRule);
  }

  /**
   * Returns the full name of a given key configuration.<br>
   * This value is used to be shown to the user.
   *
   * @return The configuration full name.
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Returns the configuration's key according to the OCPP 1.6 protocol.
   *
   * @return The configuration's key.
   */
  public String getOcpp16Key() {
    return ocpp16Key;
  }

  /**
   * Returns the configuration's key according to the OCPP 2.0.1 protocol.
   *
   * @return The configuration's key.
   */
  public String getOcpp20Key() {
    return ocpp20Key;
  }

  /**
   * Returns the configuration's {@link Component}.
   *
   * @return {@link Component}.
   */
  public Component getComponent() {
    return component;
  }

  /**
   * Returns the configuration's regex rule for the value.
   *
   * @return The regex rule which the user should respect when giving a value.
   */
  public String getRegexRule() {
    return regexRule;
  }
}
