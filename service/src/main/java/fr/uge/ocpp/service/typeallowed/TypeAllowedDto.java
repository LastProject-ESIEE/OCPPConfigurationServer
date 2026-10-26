package fr.uge.ocpp.service.typeallowed;

import fr.uge.ocpp.domain.models.TypeAllowed;

/** An allowed type exposed by the API. */
public record TypeAllowedDto(long id, String constructor, String type) {

  /**
   * Maps an allowed type domain model to the corresponding DTO.
   *
   * @param typeAllowed the domain model to map.
   * @return the corresponding DTO.
   */
  public static TypeAllowedDto of(TypeAllowed typeAllowed) {
    return new TypeAllowedDto(typeAllowed.id(), typeAllowed.constructor(), typeAllowed.type());
  }
}
