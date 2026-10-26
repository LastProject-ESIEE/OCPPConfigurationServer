package fr.uge.ocpp.service.firmware;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.service.typeallowed.TypeAllowedDto;
import java.util.Set;
import java.util.stream.Collectors;

/** A firmware exposed by the API. */
public record FirmwareDto(long id,
                          String url,
                          String version,
                          String constructor,
                          Set<TypeAllowedDto> typesAllowed) {

  /**
   * Maps a firmware domain model to the corresponding DTO.
   *
   * @param firmware the domain model to map.
   * @return the corresponding DTO.
   */
  public static FirmwareDto of(Firmware firmware) {
    return new FirmwareDto(firmware.id(), firmware.url(), firmware.version(),
        firmware.constructor(), firmware.typesAllowed() == null ? null
            : firmware.typesAllowed().typesAllowedSet().stream()
                .map(TypeAllowedDto::of).collect(Collectors.toSet()));
  }
}
