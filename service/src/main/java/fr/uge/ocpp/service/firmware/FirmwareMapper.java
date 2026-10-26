package fr.uge.ocpp.service.firmware;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import fr.uge.ocpp.service.typeallowed.TypeAllowedDto;
import java.util.HashSet;
import java.util.Set;

/** Maps the firmware DTOs to the domain models. */
final class FirmwareMapper {

  /** Creates a new mapper, this class should not be instantiated. */
  private FirmwareMapper() {
  }

  /**
   * Maps the creation values to a firmware domain model.
   *
   * @param id the identifier of the firmware to update, null for a creation.
   * @param dto the creation values.
   * @return the corresponding domain model.
   */
  static Firmware toFirmware(Long id, CreateFirmwareDto dto) {
    Set<TypeAllowed> typesAllowed = new HashSet<>();
    if (dto.typesAllowed() != null) {
      dto.typesAllowed().forEach(typeAllowed -> typesAllowed
          .add(new TypeAllowed(typeAllowed.id(), typeAllowed.constructor(),
              typeAllowed.type())));
    }
    return new Firmware(id, dto.url(), dto.version(), dto.constructor(),
        new TypesAllowed(typesAllowed));
  }
}
