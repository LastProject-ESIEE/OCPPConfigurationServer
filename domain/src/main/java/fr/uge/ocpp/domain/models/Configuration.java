package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;

public record Configuration(Long id,
                            String name,
                            String description,
                            LocalDateTime lastEdit,
                            String configuration,
                            Firmware firmware) {
}
