package fr.uge.ocpp.domain.models;

import lombok.Builder;
import lombok.With;

/** A firmware of the OCPP configuration server domain. */
@Builder
public record Firmware(
    @With Long id,
    @With String url,
    @With String version,
    @With String constructor,
    @With TypesAllowed typesAllowed) {}
