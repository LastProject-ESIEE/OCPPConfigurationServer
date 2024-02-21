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
import org.hibernate.annotations.CreationTimestamp;

/**
 * Technical class represents a technical log in the database via JPA.<br>
 * A technical log has an id, a date, a component, a criticality and the complete log.
 */
@Entity
@Table(name = "technical_log")
public class TechnicalLog {

  /**
   * Criticality enum represents different critical level that a log can have
   * in the application.<br>
   * The criticism can be :<br>
   * - INFO ;<br>
   * - WARNING :<br>
   * - ERROR.
   */
  public enum Criticality {
    INFO, WARNING, ERROR
  }

  /**
   * Component enum represents different components where a log can be created.<br>
   * The component can be :<br>
   * - BACKEND ;<br>
   * - FRONTEND :<br>
   * - WEBSOCKET :<br>
   * - DATABASE.
   */
  public enum Component {
    BACKEND, FRONTEND, WEBSOCKET, DATABASE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "date", nullable = false,
          columnDefinition = "datetime default current_timestamp")
  @CreationTimestamp
  private Timestamp date;

  @Enumerated(EnumType.STRING)
  @Column(name = "component", nullable = false)
  private Component component;

  @Enumerated(EnumType.STRING)
  @Column(name = "criticality", nullable = false)
  private Criticality criticality;

  @Column(name = "complete_log", nullable = false)
  private String completeLog;

  /**
   * Get the id of the log.
   *
   * @return id, int.
   */
  public int getId() {
    return id;
  }

  /**
   * Set the id of the log.
   *
   * @param id an int.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the date of the log.
   *
   * @return date, Timestamp.
   */
  public Timestamp getDate() {
    return date;
  }

  /**
   * Set the date of the log.
   *
   * @param date a Timestamp.
   */
  public void setDate(Timestamp date) {
    this.date = date;
  }

  /**
   * Get the component of the log.
   *
   * @return component, String.
   */
  public String getComponent() {
    return component;
  }

  /**
   * Set the component of the log.
   *
   * @param component a String.
   */
  public void setComponent(String component) {
    this.component = component;
  }

  /**
   * Get the criticism of the log.
   *
   * @return criticism, Criticality.
   */
  public Criticality getCriticality() {
    return criticality;
  }

  /**
   * Set the criticism of the log.
   *
   * @param criticality a Criticality.
   */
  public void setCriticality(Criticality criticality) {
    this.criticality = criticality;
  }

  /**
   * Get the complete log.
   *
   * @return completeLog, String.
   */
  public String getCompleteLog() {
    return completeLog;
  }

  /**
   * Set the complete log.
   *
   * @param completeLog a String.
   */
  public void setCompleteLog(String completeLog) {
    this.completeLog = completeLog;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TechnicalLog that)) {
      return false;
    }
    return getId() == that.getId()
           && Objects.equals(getDate(), that.getDate())
           && Objects.equals(getComponent(), that.getComponent())
           && getCriticality() == that.getCriticality()
           && Objects.equals(getCompleteLog(), that.getCompleteLog());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
            getId(),
            getDate(),
            getComponent(),
            getCriticality(),
            getCompleteLog());
  }

  @Override
  public String toString() {
    return "TechnicalLog{"
           + "id=" + id
           + ", date=" + date
           + ", component='" + component + '\''
           + ", criticism=" + criticality
           + ", completeLog='" + completeLog + '\''
           + '}';
  }
}
