package fr.uge.ocpp.database.entities;

import fr.uge.ocpp.domain.models.ChargePoint;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

/** A charge point persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "charge_point")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class ChargePointEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
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

  public static ChargePointEntity fromDomain(ChargePoint chargePoint) {
    return new ChargePointEntity(
        chargePoint.id(),
        chargePoint.serialNumber(),
        chargePoint.type(),
        chargePoint.constructor(),
        chargePoint.clientId(),
        chargePoint.lastUpdate(),
        chargePoint.error(),
        chargePoint.state(),
        chargePoint.step(),
        chargePoint.statusProcess(),
        ConfigurationEntity.fromDomain(chargePoint.configuration()));
  }

  public ChargePoint toDomain() {
    return ChargePoint.builder()
        .id(id)
        .serialNumber(serialNumberChargepoint)
        .type(type)
        .constructor(constructor)
        .clientId(clientId)
        .lastUpdate(lastUpdate)
        .error(error)
        .state(state)
        .step(step)
        .statusProcess(status)
        .configuration(configuration.toDomain())
        .build();
  }
}
