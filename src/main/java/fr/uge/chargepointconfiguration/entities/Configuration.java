package fr.uge.chargepointconfiguration.entities;

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
import java.util.Objects;
import java.util.Set;

/**
 * A configuration class representation in the database.
 * A configuration has an Id, a name, a description and
 * the JSON configuration.
*/
@Entity
@Table(name = "configuration")
public class Configuration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_configuration")
  private int id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "configuration", nullable = false)
  private String configuration;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "compatibility",
          joinColumns = @JoinColumn(name = "id_type_allowed"),
          inverseJoinColumns = @JoinColumn(name = "id_firmware"))
  private Set<TypeAllowed> typesAllowed;

  public int getId() {
    return id;
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

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    var configuration = (Configuration) o;
    return id == configuration.id
           && Objects.equals(name, configuration.name)
           && Objects.equals(description, configuration.description)
           && Objects.equals(configuration, configuration.configuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, configuration);
  }

  @Override
  public String toString() {
    return "Firmware{"
           + "id=" + id
           + ", name='" + name + '\''
           + ", description=" + description
           + ", configuration='" + configuration + '\''
           + '}';
  }
}
