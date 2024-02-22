package fr.uge.chargepointconfiguration.configuration;

import fr.uge.chargepointconfiguration.user.UserDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * A configuration class representation in the database.
 * A configuration has an Id, a name, a description and
 * the JSON configuration.
*/
@Entity
@Table(name = "configuration")
public class Configuration implements fr.uge.chargepointconfiguration.Entity<ConfigurationDto> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_configuration")
  private int id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description", nullable = false,
      columnDefinition = "longtext default ''")
  private String description = "";

  @Column(name = "configuration", nullable = false)
  private String configuration;

  /**
   * Configuration's constructor without defaults values.
   *
   * @param name How you want your configuration to be named.
   * @param configuration A JSON containing key and values for your configuration.
   */
  public Configuration(String name,
                       String configuration) {
    this.name = Objects.requireNonNull(name);
    this.configuration = Objects.requireNonNull(configuration);
  }

  /**
   * Empty constructor. Should not be called.
   */
  public Configuration() {

  }

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
           && Objects.equals(this.configuration, configuration.configuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, configuration);
  }

  @Override
  public ConfigurationDto toDto() {
    return new ConfigurationDto(
        id,
          name,
          description,
          configuration);
  }

  @Override
  public String toString() {
    return "Configuration{"
           + "id=" + id
           + ", name='" + name + '\''
           + ", description='" + description + '\''
           + ", configuration='" + configuration + '\''
           + '}';
  }
}
