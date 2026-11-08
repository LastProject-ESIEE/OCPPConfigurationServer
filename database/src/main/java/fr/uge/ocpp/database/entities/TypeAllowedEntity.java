package fr.uge.ocpp.database.entities;

import fr.uge.ocpp.domain.models.TypeAllowed;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** A charge point model allowed by the server, persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "type_allowed")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TypeAllowedEntity {

  /**
   * Creates an allowed type reference with the given identifier.
   *
   * @param id the identifier of the allowed type.
   * @return an allowed type reference.
   */
  public static TypeAllowedEntity reference(Long id) {
    var entity = new TypeAllowedEntity();
    entity.setId(id);
    return entity;
  }

  public static TypeAllowedEntity fromDomain(TypeAllowed typeAllowed) {
    return new TypeAllowedEntity(
        typeAllowed.id(), typeAllowed.constructor(), typeAllowed.type(), Set.of());
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @Column(name = "type", nullable = false, length = 45)
  private String type;

  @ManyToMany(mappedBy = "typesAllowed")
  private Set<FirmwareEntity> firmwares = new HashSet<>();

  public TypeAllowed toDomain() {
    return TypeAllowed.builder().id(id).constructor(constructor).type(type).build();
  }
}
