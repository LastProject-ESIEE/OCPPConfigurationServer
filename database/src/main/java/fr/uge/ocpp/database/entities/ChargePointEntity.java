package fr.uge.ocpp.database.entities;

import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;

/** A charge point persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "chargepoint")
public class ChargePointEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_chargepoint")
  private Long id;

  @Column(name = "serial_number_chargepoint", nullable = false, length = 45)
  private String serialNumberChargepoint;

  @Column(name = "type", nullable = false, length = 45)
  private String type;

  @Column(name = "constructor", nullable = false, length = 45)
  private String constructor;

  @Column(name = "client_id", nullable = false, length = 45)
  private String clientId;

  @Column(
      name = "last_update",
      nullable = false,
      columnDefinition = "timestamp default current_timestamp")
  @CreationTimestamp
  private LocalDateTime lastUpdate = LocalDateTime.now();

  @Column(name = "error", nullable = false, columnDefinition = "text default ''")
  private String error = "";

  @Column(name = "state", nullable = false, columnDefinition = "boolean default false")
  private boolean state = false;

  @Column(name = "step", nullable = false, columnDefinition = "varchar(32) default 'FIRMWARE'")
  @Enumerated(EnumType.STRING)
  private Step step = Step.FIRMWARE;

  @Column(
      name = "step_status",
      nullable = false,
      columnDefinition = "varchar(32) default 'PENDING'")
  @Enumerated(EnumType.STRING)
  private StatusProcess status = StatusProcess.PENDING;

  @ManyToOne
  @JoinColumn(name = "id_configuration", referencedColumnName = "id_configuration")
  private ConfigurationEntity configuration;

  /** Creates an empty charge point, required by JPA. */
  protected ChargePointEntity() {}

  /**
   * Creates a charge point.
   *
   * @param serialNumberChargepoint the serial number of the charge point.
   * @param type the commercial name of the charge point.
   * @param constructor the constructor of the charge point.
   * @param clientId the client identifier of the charge point.
   * @param configuration the configuration applied to the charge point.
   */
  public ChargePointEntity(
      String serialNumberChargepoint,
      String type,
      String constructor,
      String clientId,
      ConfigurationEntity configuration) {
    this.serialNumberChargepoint = Objects.requireNonNull(serialNumberChargepoint);
    this.type = Objects.requireNonNull(type);
    this.constructor = Objects.requireNonNull(constructor);
    this.clientId = Objects.requireNonNull(clientId);
    this.configuration = configuration;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(LocalDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  public Step getStep() {
    return step;
  }

  public void setStep(Step step) {
    this.step = step;
  }

  public StatusProcess getStatus() {
    return status;
  }

  public void setStatus(StatusProcess status) {
    this.status = status;
  }

  public ConfigurationEntity getConfiguration() {
    return configuration;
  }

  public void setConfiguration(ConfigurationEntity configuration) {
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
    ChargePointEntity that = (ChargePointEntity) o;
    return state == that.state
        && Objects.equals(id, that.id)
        && Objects.equals(serialNumberChargepoint, that.serialNumberChargepoint)
        && Objects.equals(type, that.type)
        && Objects.equals(constructor, that.constructor)
        && Objects.equals(clientId, that.clientId)
        && Objects.equals(lastUpdate, that.lastUpdate)
        && Objects.equals(error, that.error)
        && step == that.step
        && status == that.status
        && Objects.equals(configuration, that.configuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        serialNumberChargepoint,
        type,
        constructor,
        clientId,
        lastUpdate,
        error,
        state,
        step,
        status,
        configuration);
  }

  @Override
  public String toString() {
    return "ChargePointEntity{"
        + "id="
        + id
        + ", serialNumberChargepoint='"
        + serialNumberChargepoint
        + '\''
        + ", type='"
        + type
        + '\''
        + ", constructor='"
        + constructor
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + ", lastUpdate="
        + lastUpdate
        + ", error='"
        + error
        + '\''
        + ", state="
        + state
        + ", step="
        + step
        + ", status="
        + status
        + ", configuration="
        + (configuration == null ? null : configuration.getId())
        + '}';
  }
}
