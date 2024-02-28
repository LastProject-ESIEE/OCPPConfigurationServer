package fr.uge.chargepointconfiguration.chargepoint;

import fr.uge.chargepointconfiguration.DtoEntity;
import fr.uge.chargepointconfiguration.configuration.Configuration;
import fr.uge.chargepointconfiguration.firmware.Firmware;
import fr.uge.chargepointconfiguration.status.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Chargepoint class represents a charge point in the database via JPA.<br>
 * A Chargepoint has an ID, a serial_number_chargepoint, a type, a constructor, a client_id,
 * a server_address, a configuration, a last_edit, a {@link Status} and a {@link Firmware}.
 */
@Entity
@Table(name = "chargepoint")
public class Chargepoint implements DtoEntity<ChargepointDto> {

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

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(
      name = "id_configuration",
      referencedColumnName = "id_configuration",
      nullable = false
  )
  private Configuration configuration;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_status", referencedColumnName = "id_status", nullable = false)
  private Status status;

  /**
   * Chargepoint's constructor.
   *
   * @param serialNumberChargepoint The chargepoint's unique serial id.
   * @param type The commercial name of the chargepoint.
   * @param constructor The chargepoint's manufacturer.
   * @param clientId The client's name of the chargepoint.
   * @param serverAddress The server's URL of the chargepoint.
   * @param configuration A JSON containing the chargepoint's configuration.
   * @param status Description of the current state of configuration for the chargepoint.
   */
  public Chargepoint(String serialNumberChargepoint,
                     String type,
                     String constructor,
                     String clientId,
                     String serverAddress,
                     Configuration configuration,
                     Status status) {
    this.serialNumberChargepoint = Objects.requireNonNull(serialNumberChargepoint);
    this.type = Objects.requireNonNull(type);
    this.constructor = Objects.requireNonNull(constructor);
    this.clientId = Objects.requireNonNull(clientId);
    this.serverAddress = Objects.requireNonNull(serverAddress);
    this.configuration = Objects.requireNonNull(configuration);
    this.status = Objects.requireNonNull(status);
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


  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
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
           && Objects.equals(status, that.status);
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
            status);
  }

  @Override
  public ChargepointDto toDto() {
    return new ChargepointDto(
        id,
          serialNumberChargepoint,
          type,
          constructor,
          clientId,
          serverAddress,
          configuration.toDto(),
          status.toDto());
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
           + ", status=" + status
           + '}';
  }
}
