package fr.uge.ocpp.domain.models;

public record Firmware(Long id, String url, String version, String constructor, TypesAllowed typesAllowed) {

  public Firmware withId(Long id) {
    return new Firmware(id, url, version, constructor, typesAllowed);
  }
}
