package fr.uge.ocpp.domain.models;

/** A firmware of the OCPP configuration server domain. */
public record Firmware(
    Long id, String url, String version, String constructor, TypesAllowed typesAllowed) {

  /**
   * Returns a copy of this firmware with the given identifier.
   *
   * @param id the new identifier.
   * @return a copy of this firmware.
   */
  public Firmware withId(Long id) {
    return new Firmware(id, url, version, constructor, typesAllowed);
  }

  /**
   * Returns a copy of this firmware with the given url.
   *
   * @param url the new url.
   * @return a copy of this firmware.
   */
  public Firmware withUrl(String url) {
    return new Firmware(id, url, version, constructor, typesAllowed);
  }

  /**
   * Returns a copy of this firmware with the given version.
   *
   * @param version the new version.
   * @return a copy of this firmware.
   */
  public Firmware withVersion(String version) {
    return new Firmware(id, url, version, constructor, typesAllowed);
  }

  /**
   * Returns a copy of this firmware with the given constructor.
   *
   * @param constructor the new constructor.
   * @return a copy of this firmware.
   */
  public Firmware withConstructor(String constructor) {
    return new Firmware(id, url, version, constructor, typesAllowed);
  }

  /**
   * Returns a copy of this firmware with the given allowed types.
   *
   * @param typesAllowed the new allowed types.
   * @return a copy of this firmware.
   */
  public Firmware withTypesAllowed(TypesAllowed typesAllowed) {
    return new Firmware(id, url, version, constructor, typesAllowed);
  }
}
