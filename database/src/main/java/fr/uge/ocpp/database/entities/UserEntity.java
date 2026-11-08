package fr.uge.ocpp.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
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
}
