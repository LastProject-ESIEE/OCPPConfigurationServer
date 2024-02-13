package fr.uge.chargepointconfiguration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Status class represents a status in the database via JPA.<br>
 * A Status has an ID, a last_update, an error, a state, a {@link Step},
 * and a {@link Status}.
 */
@Entity
@Table(name = "status")
public class Status {
  private enum Step { firmware, configuration }

  private enum StatusProcess { pending, processing, finished, failed }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_status")
  private int id;

  @Column(name = "last_update", nullable = false)
  private Timestamp lastUpdate;

  @Column(name = "error", nullable = false, length = 65_535)
  private String error;

  @Column(name = "state", nullable = false)
  private boolean state;

  @Column(name = "step", nullable = false)
  @Enumerated(EnumType.STRING)
  private Step step;

  @Column(name = "step_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private StatusProcess status;

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Timestamp getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
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

  @Override
  public String toString() {
    return "Status{"
           + ", lastUpdate=" + lastUpdate
           + ", error='" + error + '\''
           + ", state=" + state
           + ", step=" + step
           + ", status=" + status
           + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Status status1 = (Status) o;
    return id == status1.id
           && state == status1.state
           && Objects.equals(lastUpdate, status1.lastUpdate)
           && Objects.equals(error, status1.error)
           && step == status1.step
           && status == status1.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, lastUpdate, error, state, step, status);
  }
}
