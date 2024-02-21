package fr.uge.chargepointconfiguration.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Chargepoint class represents a charge point in the database via JPA.<br>
 * A Chargepoint has an ID, a serial_number_chargepoint, a type, a constructor, a client_id,
 * a server_address, a configuration, a last_edit, a {@link Status} and a {@link Firmware}.
 */
@Entity
@Table(name = "chargepoint")
public class Chargepoint {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_chargepoint")
  private int id;

  @Column(name = "serial_number_chargepoint", nullable = false, length = 45)
  private String serialNumberChargepoint;

  @Column(name = "type", nullable = false, length = 45)
  private String type;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @Column(name = "client_id", nullable = false, length = 45)
  private String clientId;

  @Column(name = "server_address", nullable = false, length = 65_535)
  private String serverAddress;

  @Column(name = "last_edit", nullable = false,
      columnDefinition = "datetime default current_timestamp")
  @CreationTimestamp
  private Timestamp lastEdit;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
  private Status status;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_firmware", referencedColumnName = "id_firmware", nullable = false)
  private Firmware firmware;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(
          name = "id_configuration",
          referencedColumnName = "id_configuration",
          nullable = false
  )
  private Configuration configuration;

  /**
   * Chargepoint's constructor.
   *
   * @param serialNumberChargepoint The chargepoint's unique serial id.
   * @param type The commercial name of the chargepoint.
   * @param constructor The chargepoint's manufacturer.
   * @param clientId The client's name of the chargepoint.
   * @param serverAddress The server's URL of the chargepoint.
   * @param configuration A JSON containing the chargepoint's configuration.
   * @param firmware The chargepoint's firmware.
   */
  public Chargepoint(String serialNumberChargepoint,
                     String type,
                     String constructor,
                     String clientId,
                     String serverAddress,
                     Configuration configuration,
                     Status status,
                     Firmware firmware) {
    this.serialNumberChargepoint = Objects.requireNonNull(serialNumberChargepoint);
    this.type = Objects.requireNonNull(type);
    this.constructor = Objects.requireNonNull(constructor);
    this.clientId = Objects.requireNonNull(clientId);
    this.serverAddress = Objects.requireNonNull(serverAddress);
    this.configuration = Objects.requireNonNull(configuration);
    this.firmware = Objects.requireNonNull(firmware);
    this.status = Objects.requireNonNull(status);
    lastEdit = status.getLastUpdate();
  }

  /**
   * Empty constructor. Should not be called.
   */
  public Chargepoint() {
  }

  public int getId() {
    return id;
  }

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

  public Configuration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(Configuration configuration) {
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Chargepoint that = (Chargepoint) o;
    return id == that.id && Objects.equals(serialNumberChargepoint, that.serialNumberChargepoint)
           && Objects.equals(type, that.type)
           && Objects.equals(constructor, that.constructor)
           && Objects.equals(clientId, that.clientId)
           && Objects.equals(serverAddress, that.serverAddress)
           && Objects.equals(configuration, that.configuration)
           && Objects.equals(lastEdit, that.lastEdit)
           && Objects.equals(status, that.status)
           && Objects.equals(firmware, that.firmware);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id,
            serialNumberChargepoint,
            type,
            constructor,
            clientId,
            serverAddress,
            configuration,
            lastEdit,
            status,
            firmware);
  }

  @Override
  public String toString() {
    return "Chargepoint{"
           + "id=" + id
           + ", serialNumberChargepoint='" + serialNumberChargepoint + '\''
           + ", type='" + type + '\''
           + ", constructor='" + constructor + '\''
           + ", clientId='" + clientId + '\''
           + ", serverAddress='" + serverAddress + '\''
           + ", configuration='" + configuration + '\''
           + ", lastEdit=" + lastEdit
           + ", status=" + status
           + ", firmware=" + firmware
           + '}';
  }
}
