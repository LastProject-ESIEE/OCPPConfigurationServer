package fr.uge.chargepointconfiguration.logs.sealed;

import fr.uge.chargepointconfiguration.DtoEntity;
import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.logs.business.BusinessLogDto;
import fr.uge.chargepointconfiguration.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;

/**
 * BusinessLog class represents a Business log in the database via JPA.<br>
 * A business log has an id, a date, an user,
 * a charge point, a firmware version and the complete log.
 */
@Entity
@Table(name = "business_log")
public final class BusinessLog implements DtoEntity<BusinessLogDto>, Log {

  /**
   * Category attach to this log.<br>
   * - LOGIN : log in and out of chargepoints.<br>
   * - STATUS : any creation/modification/delete of the status of a chargepoint.<br>
   * - FIRM : any creation/modification/delete of a firmware.<br>
   * - CONFIG : any creation/modification/delete of a configuration.<br>
   */
  public enum Category {
    LOGIN, STATUS, FIRM, CONFIG
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "date", nullable = false,
          columnDefinition = "datetime default current_timestamp")
  @CreationTimestamp
  private Timestamp date;

  /*
   * The quote for user ("user") are here to specify the database H2 that
   * user isn't the key word user, but a field user in the database.
   */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "chargepoint_id", referencedColumnName = "id_chargepoint")
  private Chargepoint chargepoint;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private Category category;

  @Column(name = "complete_log", nullable = false)
  private String completeLog;

  /**
   * BusinessLog's constructor.
   *
   * @param user User logged currently implied with this log, null if not.
   * @param chargepoint Chargepoint implied with this log, null if not.
   * @param category {@link TechnicalLog.Component}
   * @param completeLog All the log in a String.
   */
  public BusinessLog(User user, Chargepoint chargepoint, Category category, String completeLog) {
    this.user = user;
    this.chargepoint = chargepoint;
    this.category = Objects.requireNonNull(category);
    this.completeLog = Objects.requireNonNull(completeLog);
    date = new Timestamp(System.currentTimeMillis());
  }

  /**
   * Empty constructor. Should not be called.
   */
  public BusinessLog() {

  }

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
  public User getUser() {
    return user;
  }

  /**
   * Set the mail user of the log.
   *
   * @param user a String.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
  * Get the id of the charge point of the log.
  *
  * @return chargePoint, int.
  */
  public Chargepoint getChargepoint() {
    return chargepoint;
  }

  /**
   * Set the id of the charge point.
   *
   * @param chargepoint an int.
   */
  public void setChargepoint(Chargepoint chargepoint) {
    this.chargepoint = chargepoint;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
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
           && getChargepoint() == that.getChargepoint()
           && Objects.equals(getDate(), that.getDate())
           && Objects.equals(getUser(), that.getUser())
           && Objects.equals(getCompleteLog(), that.getCompleteLog());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(),
            getDate(),
            getUser(),
            getChargepoint(),
            getCompleteLog());
  }

  @Override
  public BusinessLogDto toDto() {
    return new BusinessLogDto(this.id,
        this.date,
        this.user,
        this.chargepoint,
        this.category,
        this.completeLog);
  }

  @Override
  public String text() {
    return date + " "
           + "{" + category + "} "
           + "(" + id + ") "
           + "user " + user.getId() + " "
           + "chargepoint " + chargepoint + " "
           + completeLog;
  }

  @Override
  public String toString() {
    return "BusinessLog{"
           + "id=" + id
           + ", date=" + date
           + ", user='" + user + '\''
           + ", chargePoint='" + chargepoint + '\''
           + ", category='" + category + '\''
           + ", completeLog='" + completeLog + '\''
           + '}';
  }
}
