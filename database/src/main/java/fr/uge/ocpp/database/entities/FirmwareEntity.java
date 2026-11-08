package fr.uge.ocpp.database.entities;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.TypesAllowed;
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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** A firmware persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "firmware")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class FirmwareEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
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

  public static FirmwareEntity fromDomain(Firmware firmware) {
    TypesAllowed typesAllowed =
        Objects.requireNonNullElseGet(firmware.typesAllowed(), TypesAllowed::empty);
    return new FirmwareEntity(
        firmware.id(),
        firmware.url(),
        firmware.version(),
        firmware.constructor(),
        typesAllowed.stream().map(TypeAllowedEntity::fromDomain).collect(toSet()));
  }

  public Firmware toDomain() {
    return Firmware.builder()
        .id(id)
        .url(url)
        .version(version)
        .constructor(constructor)
        .typesAllowed(
            typesAllowed.stream()
                .map(TypeAllowedEntity::toDomain)
                .collect(collectingAndThen(toSet(), TypesAllowed::new)))
        .build();
  }
}
