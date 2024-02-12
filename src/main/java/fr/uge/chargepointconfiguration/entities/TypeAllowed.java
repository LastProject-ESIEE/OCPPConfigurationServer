package fr.uge.chargepointconfiguration.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "type_allowed")
public class TypeAllowed {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_type_allowed")
  private int id;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @Column(name = "type", nullable = false, length = 45)
  private String type;

//  @ManyToMany(mappedBy = "typesAllowed")  // TODO boucle infini entre firmware et typeallowed (normal)
//  Set<Firmware> firmwaresAllowed;

//  public int getId() {
//    return id;
//  }

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

//  public Set<Firmware> getFirmwaresAllowed() {
//    return firmwaresAllowed;
//  }
//
//  public void setFirmwaresAllowed(Set<Firmware> firmwaresAllowed) {
//    this.firmwaresAllowed = firmwaresAllowed;
//  }
}
