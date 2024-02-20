package fr.uge.chargepointconfiguration;


/**
 * Common interface for entities.
 *
 * @param <D> The DTO matching this entity
 */
public interface Entity<D> {
  D toDto();
}
