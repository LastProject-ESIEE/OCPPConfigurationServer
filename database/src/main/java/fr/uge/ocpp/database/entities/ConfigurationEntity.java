package fr.uge.ocpp.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;

/** A configuration persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "configuration")
public class ConfigurationEntity {

  /**
   * Creates a configuration reference with the given identifier.
   *
   * @param id the identifier of the configuration.
   * @return a configuration reference.
   */
  public static ConfigurationEntity reference(Long id) {
    var entity = new ConfigurationEntity();
    entity.setId(id);
    return entity;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_configuration")
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description", nullable = false, columnDefinition = "text default ''")
  private String description = "";

  @Column(name = "last_edit", nullable = false,
      columnDefinition = "timestamp default current_timestamp")
  @CreationTimestamp
  private LocalDateTime lastEdit = LocalDateTime.now();

  @Column(name = "configuration", nullable = false, columnDefinition = "text")
  private String configuration;

  @ManyToOne
  @JoinColumn(name = "id_firmware", referencedColumnName = "id_firmware")
  private FirmwareEntity firmware;

  /** Creates an empty configuration, required by JPA. */
  protected ConfigurationEntity() {
  }

  /**
   * Creates a configuration.
   *
   * @param name          the name of the configuration.
   * @param description   the description of the configuration.
   * @param configuration the content of the configuration.
   * @param firmware      the firmware of the configuration.
   */
  public ConfigurationEntity(String name,
                             String description,
                             String configuration,
                             FirmwareEntity firmware) {
    this.name = Objects.requireNonNull(name);
    this.description = Objects.requireNonNullElse(description, "");
    this.configuration = Objects.requireNonNull(configuration);
    this.firmware = firmware;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getLastEdit() {
    return lastEdit;
  }

  public void setLastEdit(LocalDateTime lastEdit) {
    this.lastEdit = lastEdit;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public FirmwareEntity getFirmware() {
    return firmware;
  }

  public void setFirmware(FirmwareEntity firmware) {
    this.firmware = firmware;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurationEntity that = (ConfigurationEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name)
        && Objects.equals(description, that.description)
        && Objects.equals(lastEdit, that.lastEdit)
        && Objects.equals(configuration, that.configuration)
        && Objects.equals(firmware, that.firmware);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, lastEdit, configuration, firmware);
  }

  @Override
  public String toString() {
    return "ConfigurationEntity{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", description='" + description + '\''
        + ", lastEdit=" + lastEdit
        + ", configuration='" + configuration + '\''
        + ", firmware=" + (firmware == null ? null : firmware.getId())
        + '}';
  }
}
