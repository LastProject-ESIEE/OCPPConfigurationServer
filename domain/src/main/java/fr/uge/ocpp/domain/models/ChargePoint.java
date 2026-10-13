package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;

public record ChargePoint(Long id,
                          String serialNumber,
                          String type,
                          String constructor,
                          LocalDateTime lastUpdate,
                          String error,
                          boolean state,
                          Step step,
                          StatusProcess statusProcess,
                          Configuration configuration) {
}
