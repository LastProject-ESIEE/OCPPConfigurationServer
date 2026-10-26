package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import java.util.Optional;

/** SPI port to persist and retrieve allowed types. */
public interface TypeAllowedRepository {

  /**
   * Finds all the allowed types.
   *
   * @return all the allowed types.
   */
  TypesAllowed findAll();

  /**
   * Finds the allowed type with the given identifier.
   *
   * @param id the identifier of the allowed type.
   * @return the corresponding allowed type if any.
   */
  Optional<TypeAllowed> findById(long id);

  /**
   * Saves the given allowed type.
   *
   * @param typeAllowed the allowed type to save.
   * @return the saved allowed type.
   */
  TypeAllowed save(TypeAllowed typeAllowed);
}
