package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/** A configuration of the OCPP configuration server domain. */
@Builder
public record Configuration(
    @With Long id,
    @With String name,
    @With String description,
    @With LocalDateTime lastEdit,
    @With String configuration,
    @With Firmware firmware) {}
