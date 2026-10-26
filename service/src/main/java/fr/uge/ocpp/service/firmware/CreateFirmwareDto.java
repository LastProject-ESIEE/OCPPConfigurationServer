package fr.uge.ocpp.service.firmware;

import fr.uge.ocpp.service.typeallowed.TypeAllowedDto;
import java.util.List;

/** The values required to create or update a firmware. */
public record CreateFirmwareDto(
    String url, String version, String constructor, List<TypeAllowedDto> typesAllowed) {}
