package fr.uge.chargepointconfiguration.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "status")
public class Status {
  private enum Step{ firmware, configuration }
  private enum StatusProcess{ pending, processing, finished, failed }// corresponds à l'attribut status de la table Status (il ya avait un comflit entre
    // le nom de la classe et le nom de l'enum)
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

//    @OneToOne(mappedBy = "status")
//    private Chargepoint chargepoint;

//    public int getId() {
//    return id;
//  }

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

//  public Chargepoint getChargepoint() {
//    return chargepoint;
//  }
//
//  public void setChargepoint(Chargepoint chargepoint) {
//    this.chargepoint = chargepoint;
//  }

  @Override
  public String toString() {
    return "Status{" +
      ", lastUpdate=" + lastUpdate +
      ", error='" + error + '\'' +
      ", state=" + state +
      ", step=" + step +
      ", status=" + status +
//      ", chargepoint=" + chargepoint +
      '}';
  }
}
