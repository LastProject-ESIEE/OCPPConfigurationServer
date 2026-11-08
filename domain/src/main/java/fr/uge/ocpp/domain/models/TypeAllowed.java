package fr.uge.ocpp.domain.models;

import lombok.Builder;

/** A charge point model allowed by the server. */
@Builder
public record TypeAllowed(Long id, String constructor, String type) {}
