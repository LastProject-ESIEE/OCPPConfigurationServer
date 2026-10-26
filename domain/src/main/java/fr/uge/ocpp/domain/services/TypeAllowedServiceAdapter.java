package fr.uge.ocpp.domain.services;

import fr.uge.ocpp.domain.annotations.DomainService;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import fr.uge.ocpp.domain.ports.api.TypeAllowedService;
import fr.uge.ocpp.domain.ports.spi.TypeAllowedRepository;
import java.util.Objects;

/** Adapter implementing the allowed type use cases on top of the SPI ports. */
@DomainService
public record TypeAllowedServiceAdapter(TypeAllowedRepository typeAllowedRepository)
    implements TypeAllowedService {

  /**
   * Creates the adapter.
   *
   * @param typeAllowedRepository the allowed type persistence port.
   */
  public TypeAllowedServiceAdapter {
    Objects.requireNonNull(typeAllowedRepository);
  }

  /**
   * Finds all the allowed types.
   *
   * @return all the allowed types.
   */
  @Override
  public TypesAllowed findAll() {
    return typeAllowedRepository.findAll();
  }

  /**
   * Creates an allowed type.
   *
   * @param typeAllowed the allowed type to create.
   * @return the created allowed type.
   */
  @Override
  public TypeAllowed save(TypeAllowed typeAllowed) {
    Objects.requireNonNull(typeAllowed);
    return typeAllowedRepository
        .save(new TypeAllowed(null, typeAllowed.constructor(), typeAllowed.type()));
  }
}
