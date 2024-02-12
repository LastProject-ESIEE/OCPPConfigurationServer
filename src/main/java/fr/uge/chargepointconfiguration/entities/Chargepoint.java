package fr.uge.chargepointconfiguration.entities;


import jakarta.persistence.*;
import org.springframework.context.annotation.Primary;

import java.sql.Timestamp;

@Entity
@Table(name = "chargepoint")
public class Chargepoint {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_chargepoint")
  private int id;

  @Column(name = "serial_number_chargepoint", nullable = false, length = 45)
  private String serialNumberChargepoint;

  @Column(name = "type",nullable = false, length = 45)
  private String type;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @Column(name = "client_id", nullable = false, length = 45)
  private String clientId;

  @Column(name = "server_address", nullable = false, length = 65_535)
  private String serverAddress;

  @Column(name = "configuration", nullable = false)
  private String configuration;

  @Column(name = "last_edit", nullable = false)
  private Timestamp lastEdit;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
  private Status status;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // TODO juger s'il y a un réel besoin d'avoir le firmware tout le temps
  @JoinColumn(name = "id_firmware", referencedColumnName = "id_firmware", nullable = false)
  private Firmware firmware;

//  public int getId() {
//    return id;
//  }

  public String getSerialNumberChargepoint() {
    return serialNumberChargepoint;
  }

  public void setSerialNumberChargepoint(String serialNumberChargepoint) {
    this.serialNumberChargepoint = serialNumberChargepoint;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getConstructor() {
    return constructor;
  }

  public void setConstructor(String constructor) {
    this.constructor = constructor;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getServerAddress() {
    return serverAddress;
  }

  public void setServerAddress(String serverAddress) {
    this.serverAddress = serverAddress;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public Timestamp getLastEdit() {
    return lastEdit;
  }

  public void setLastEdit(Timestamp lastEdit) {
    this.lastEdit = lastEdit;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Firmware getFirmware() {
    return firmware;
  }

  public void setFirmware(Firmware firmware) {
    this.firmware = firmware;
  }

  @Override
  public String toString() {
    return "Chargepoint{" +
      "id=" + id +
      ", serialNumberChargepoint='" + serialNumberChargepoint + '\'' +
      ", type='" + type + '\'' +
      ", constructor='" + constructor + '\'' +
      ", clientId='" + clientId + '\'' +
      ", serverAddress='" + serverAddress + '\'' +
      ", configuration='" + configuration + '\'' +
      ", lastEdit=" + lastEdit +
      ", status=" + status +
      ", firmware=" + firmware +
      '}';
  }
}
