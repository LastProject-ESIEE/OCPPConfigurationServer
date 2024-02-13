package fr.uge.chargepointconfiguration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Firmware class represents a firmware in the database via JPA.<br>
 * A Firmware has an ID, a URL, a majorVersion, a minorVersion, a constructor,
 * and some {@link TypeAllowed}.
 */
@Entity
@Table(name = "firmware")
public class Firmware {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_firmware")
  private int id;

  @Column(name = "url", nullable = false, length = 65_535)
  private String url;

  @Column(name = "major_version", nullable = false)
  private int majorVersion;

  @Column(name = "minor_version", nullable = false)
  private int minorVersion;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  //  @OneToMany(cascade = CascadeType.ALL, mappedBy = "firmware")
  //  private List<Chargepoint> chargepoints;

  @ManyToMany
  @JoinTable(name = "compatibility",
          joinColumns = @JoinColumn(name = "id_firmware"),
          inverseJoinColumns = @JoinColumn(name = "id_type_allowed"))
  private Set<TypeAllowed> typesAllowed;

  public int getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getMajorVersion() {
    return majorVersion;
  }

  public void setMajorVersion(int majorVersion) {
    this.majorVersion = majorVersion;
  }

  public int getMinorVersion() {
    return minorVersion;
  }

  public void setMinorVersion(int minorVersion) {
    this.minorVersion = minorVersion;
  }

  public String getConstructor() {
    return constructor;
  }

  public void setConstructor(String constructor) {
    this.constructor = constructor;
  }

  //  public List<Chargepoint> getChargepoints() {
  //    return chargepoints;
  //  }
  //
  //  public void setChargepoints(List<Chargepoint> chargepoints) {
  //    this.chargepoints = chargepoints;
  //  }

  public Set<TypeAllowed> getTypesAllowed() {
    return typesAllowed;
  }

  public void setTypesAllowed(Set<TypeAllowed> typesAllowed) {
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
    Firmware firmware = (Firmware) o;
    return id == firmware.id
           && majorVersion == firmware.majorVersion
           && minorVersion == firmware.minorVersion
           && Objects.equals(url, firmware.url)
           && Objects.equals(constructor, firmware.constructor)
           && Objects.equals(typesAllowed, firmware.typesAllowed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, url, majorVersion, minorVersion, constructor, typesAllowed);
  }

  @Override
  public String toString() {
    return "Firmware{"
           + "id=" + id
           + ", url='" + url + '\''
           + ", majorVersion=" + majorVersion
           + ", minorVersion=" + minorVersion
           + ", constructor='" + constructor + '\''
           + ", typesAllowed=" + typesAllowed
           + '}';
  }
}
