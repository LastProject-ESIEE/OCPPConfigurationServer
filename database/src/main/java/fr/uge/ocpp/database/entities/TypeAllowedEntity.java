package fr.uge.ocpp.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** A charge point model allowed by the server, persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "type_allowed")
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_type_allowed")
  private Long id;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @Column(name = "type", nullable = false, length = 45)
  private String type;

  @ManyToMany(mappedBy = "typesAllowed")
  private Set<FirmwareEntity> firmwares = new HashSet<>();

  /** Creates an empty allowed type, required by JPA. */
  protected TypeAllowedEntity() {
  }

  /**
   * Creates an allowed type.
   *
   * @param constructor the constructor of the charge point model.
   * @param type        the commercial name of the charge point model.
   */
  public TypeAllowedEntity(String constructor, String type) {
    this.constructor = Objects.requireNonNull(constructor);
    this.type = Objects.requireNonNull(type);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getConstructor() {
    return constructor;
  }

  public void setConstructor(String constructor) {
    this.constructor = constructor;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeAllowedEntity that = (TypeAllowedEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(constructor, that.constructor)
        && Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, constructor, type);
  }

  @Override
  public String toString() {
    return "TypeAllowedEntity{"
        + "id=" + id
        + ", constructor='" + constructor + '\''
        + ", type='" + type + '\''
        + '}';
  }
}
