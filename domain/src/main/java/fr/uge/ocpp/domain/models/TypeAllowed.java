package fr.uge.ocpp.domain.models;

/** A charge point model allowed by the server. */
public record TypeAllowed(Long id, String constructor, String type) {
}
