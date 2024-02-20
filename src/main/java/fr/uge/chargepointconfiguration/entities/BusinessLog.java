package fr.uge.chargepointconfiguration.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * BusinessLog class represents a Business log in the database via JPA.<br>
 * A business log has an id, a date, an user,
 * a charge point, a firmware version and the complete log.
 */
@Entity
@Table(name = "\"business_log\"")
public class BusinessLog {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "date", nullable = false)
  private Timestamp date;

  @Column(name = "user", nullable = false, length = 45)
  private String user;

  @Column(name = "charge_point", nullable = false)
  private int chargePoint;

  @Column(name = "firmware_version", nullable = false, length = 25)
  private String firmwareVersion;

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
   * @param date an Timestamp.
   */
  public void setDate(Timestamp date) {
    this.date = date;
  }

  /**
   * Get the user of the log.
   *
   * @return user, String
   */
  public String getUser() {
    return user;
  }

  /**
   * Set the mail user of the log.
   *
   * @param user a String.
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * Get the id of the charge point of the log.
   *
   * @return chargePoint, int.
   */
  public int getChargePoint() {
    return chargePoint;
  }

  /**
   * Set the id of the charge point.
   *
   * @param chargePoint an int.
   */
  public void setChargePoint(int chargePoint) {
    this.chargePoint = chargePoint;
  }

  /**
   * Get the firmware version of the charge point in the log.
   *
   * @return firmwareVersion, String.
   */
  public String getFirmwareVersion() {
    return firmwareVersion;
  }

  /**
   * Set the firmware version of the charge point in the log.
   *
   * @param firmwareVersion a String.
   */
  public void setFirmwareVersion(String firmwareVersion) {
    this.firmwareVersion = firmwareVersion;
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
    if (!(o instanceof BusinessLog that)) {
      return false;
    }
    return getId() == that.getId()
      && getChargePoint() == that.getChargePoint()
      && Objects.equals(getDate(), that.getDate())
      && Objects.equals(getUser(), that.getUser())
      && Objects.equals(getFirmwareVersion(), that.getFirmwareVersion())
      && Objects.equals(getCompleteLog(), that.getCompleteLog());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(),
      getDate(),
      getUser(),
      getChargePoint(),
      getFirmwareVersion(),
      getCompleteLog());
  }

  @Override
  public String toString() {
    return "BusinessLog{"
      + "id=" + id
      + ", date=" + date
      + ", user='" + user + '\''
      + ", chargePoint=" + chargePoint
      + ", firmwareVersion='" + firmwareVersion + '\''
      + ", completeLog='" + completeLog + '\''
      + '}';
  }
}
