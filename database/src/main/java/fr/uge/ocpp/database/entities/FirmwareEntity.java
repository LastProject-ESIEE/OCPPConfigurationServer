package fr.uge.ocpp.database.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** A firmware persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "firmware")
public class FirmwareEntity {

  /**
   * Creates a firmware reference with the given identifier.
   *
   * @param id the identifier of the firmware.
   * @return a firmware reference.
   */
  public static FirmwareEntity reference(Long id) {
    var entity = new FirmwareEntity();
    entity.setId(id);
    return entity;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_firmware")
  private Long id;

  @Column(name = "url", nullable = false, columnDefinition = "text")
  private String url;

  @Column(name = "version", nullable = false, length = 45)
  private String version;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "compatibility",
      joinColumns = @JoinColumn(name = "id_firmware"),
      inverseJoinColumns = @JoinColumn(name = "id_type_allowed"))
  private Set<TypeAllowedEntity> typesAllowed = new HashSet<>();

  /** Creates an empty firmware, required by JPA. */
  protected FirmwareEntity() {}

  /**
   * Creates a firmware.
   *
   * @param url the url of the firmware.
   * @param version the version of the firmware.
   * @param constructor the constructor of the firmware.
   * @param typesAllowed the charge point models compatible with the firmware.
   */
  public FirmwareEntity(
      String url, String version, String constructor, Set<TypeAllowedEntity> typesAllowed) {
    this.url = Objects.requireNonNull(url);
    this.version = Objects.requireNonNull(version);
    this.constructor = Objects.requireNonNull(constructor);
    this.typesAllowed = Objects.requireNonNullElseGet(typesAllowed, HashSet::new);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getConstructor() {
    return constructor;
  }

  public void setConstructor(String constructor) {
    this.constructor = constructor;
  }

  public Set<TypeAllowedEntity> getTypesAllowed() {
    return typesAllowed;
  }

  public void setTypesAllowed(Set<TypeAllowedEntity> typesAllowed) {
    this.typesAllowed = typesAllowed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FirmwareEntity that = (FirmwareEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(url, that.url)
        && Objects.equals(version, that.version)
        && Objects.equals(constructor, that.constructor)
        && Objects.equals(typesAllowed, that.typesAllowed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, url, version, constructor, typesAllowed);
  }

  @Override
  public String toString() {
    return "FirmwareEntity{"
        + "id="
        + id
        + ", url='"
        + url
        + '\''
        + ", version='"
        + version
        + '\''
        + ", constructor='"
        + constructor
        + '\''
        + ", typesAllowed="
        + typesAllowed
        + '}';
  }
}
