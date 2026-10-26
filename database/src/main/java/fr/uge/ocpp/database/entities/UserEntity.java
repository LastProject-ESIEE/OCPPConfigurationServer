package fr.uge.ocpp.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/** An application user persisted with JPA in PostgreSQL. */
@Entity
@Table(name = "app_user")
@SQLDelete(
    sql =
        """
      update app_user
      set is_deleted = true
      where id = ?
      """)
@SQLRestriction("not is_deleted")
public class UserEntity {

  /** The role of an application user. */
  public enum Role {
    /** A user that can only visualize data. */
    VISUALIZER,
    /** A user that can edit data. */
    EDITOR,
    /** A user that administrates the application. */
    ADMINISTRATOR
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "email", nullable = false, unique = true, length = 45)
  private String email;

  @Column(name = "lastname", nullable = false, length = 45)
  private String lastName;

  @Column(name = "firstname", nullable = false, length = 45)
  private String firstName;

  @Column(name = "password", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
  private boolean isDeleted = false;

  /** Creates an empty user, required by JPA. */
  protected UserEntity() {}

  /**
   * Creates a user.
   *
   * @param firstName the first name of the user.
   * @param lastName the last name of the user.
   * @param email the email of the user.
   * @param password the encoded password of the user.
   * @param role the role of the user.
   */
  public UserEntity(String firstName, String lastName, String email, String password, Role role) {
    this.firstName = Objects.requireNonNull(firstName);
    this.lastName = Objects.requireNonNull(lastName);
    this.email = Objects.requireNonNull(email);
    this.password = Objects.requireNonNull(password);
    this.role = Objects.requireNonNull(role);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserEntity that = (UserEntity) o;
    return isDeleted == that.isDeleted
        && Objects.equals(id, that.id)
        && Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, lastName, firstName, password, role, isDeleted);
  }

  @Override
  public String toString() {
    return "UserEntity{"
        + "id="
        + id
        + ", email='"
        + email
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", role="
        + role
        + ", isDeleted="
        + isDeleted
        + '}';
  }
}
