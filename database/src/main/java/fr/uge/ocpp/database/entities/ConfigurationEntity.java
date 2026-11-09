package fr.uge.ocpp.database.entities;

import fr.uge.ocpp.domain.models.Configuration;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

/** A configuration persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "configuration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ConfigurationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description", nullable = false, columnDefinition = "text default ''")
  private String description = "";

  @Column(
      name = "last_edit",
      nullable = false,
      columnDefinition = "timestamp default current_timestamp")
  @CreationTimestamp
  private LocalDateTime lastEdit = LocalDateTime.now();

  @Column(name = "configuration", nullable = false, columnDefinition = "text")
  private String configuration;

  @ManyToOne
  @JoinColumn(name = "id_firmware", referencedColumnName = "id")
  private FirmwareEntity firmware;

  public static ConfigurationEntity fromDomain(Configuration configuration) {
    return new ConfigurationEntity(
        configuration.id(),
        configuration.name(),
        configuration.description(),
        configuration.lastEdit(),
        configuration.configuration(),
        FirmwareEntity.fromDomain(configuration.firmware()));
  }

  public Configuration toDomain() {
    return Configuration.builder()
        .id(id)
        .name(name)
        .description(description)
        .lastEdit(lastEdit)
        .configuration(configuration)
        .firmware(firmware == null ? null : firmware.toDomain())
        .build();
  }
}
