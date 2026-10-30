package fr.uge.ocpp.domain.ports.api;

import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;

/** API port exposing the allowed type use cases. */
public interface TypeAllowedService {

  /**
   * Finds all the allowed types.
   *
   * @return all the allowed types.
   */
  TypesAllowed findAll();

  /**
   * Creates an allowed type.
   *
   * @param typeAllowed the allowed type to create.
   * @return the created allowed type.
   */
  TypeAllowed save(TypeAllowed typeAllowed);
}
