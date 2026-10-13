package fr.uge.ocpp.domain.models;

public record Firmware(Long id, String url, String version, String constructor, TypesAllowed typesAllowed) {
}
