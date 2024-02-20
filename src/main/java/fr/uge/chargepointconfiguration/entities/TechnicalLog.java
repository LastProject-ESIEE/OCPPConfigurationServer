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
 * Technical class represents a technical log in the database via JPA.<br>
 * A technical log has an id, a date, a component, a criticism and the complete log.
 */
@Entity
@Table(name = "\"technical_log\"")
public class TechnicalLog {

  /**
   * Role enum represents different critical level that a log can have in the application.<br>
   * The criticism can be :<br>
   * - INFO ;<br>
   * - WARNING :<br>
   * - ERROR.
   */
  public enum Criticism {
    INFO, WARNING, ERROR
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "date", nullable = false)
  private Timestamp date;

  @Column(name = "component", nullable = false, length = 45)
  private String component;

  @Enumerated(EnumType.STRING)
  @Column(name = "criticism", nullable = false)
  private Criticism criticism;

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
   * @return criticism, Criticism.
   */
  public Criticism getCriticism() {
    return criticism;
  }

  /**
   * Set the criticism of the log.
   *
   * @param criticism a Criticism.
   */
  public void setCriticism(Criticism criticism) {
    this.criticism = criticism;
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
      && getCriticism() == that.getCriticism()
      && Objects.equals(getCompleteLog(), that.getCompleteLog());
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      getId(),
      getDate(),
      getComponent(),
      getCriticism(),
      getCompleteLog());
  }

  @Override
  public String toString() {
    return "TechnicalLog{"
      + "id=" + id
      + ", date=" + date
      + ", component='" + component + '\''
      + ", criticism=" + criticism
      + ", completeLog='" + completeLog + '\''
      + '}';
  }
}
