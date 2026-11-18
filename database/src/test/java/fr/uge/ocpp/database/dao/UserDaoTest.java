package fr.uge.ocpp.database.dao;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.database.entities.UserEntity;
import fr.uge.ocpp.database.entities.UserEntity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/** Integration test of {@link UserDao} against a real (H2) database. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDaoTest {

  @Autowired private UserDao userDao;

  @Test
  void should_find_by_email() {
    // Given a persisted user
    var user =
        UserEntity.builder()
            .email("john.doe@example.com")
            .lastName("Doe")
            .firstName("John")
            .password("secret")
            .role(Role.EDITOR)
            .build();
    userDao.save(user);

    // When the user is looked up by email
    UserEntity found = userDao.findByEmail("john.doe@example.com");

    // Then the persisted user is returned with all its fields
    assertThat(found).isNotNull();
    assertThat(found.getId()).isNotNull();
    assertThat(found.getLastName()).isEqualTo("Doe");
    assertThat(found.getFirstName()).isEqualTo("John");
    assertThat(found.getRole()).isEqualTo(Role.EDITOR);
    assertThat(found.isDeleted()).isFalse();
  }

  @Test
  void should_return_null_when_email_unknown() {
    // Given an empty database
    // When an unknown email is looked up
    UserEntity found = userDao.findByEmail("nobody@example.com");

    // Then nothing is returned
    assertThat(found).isNull();
  }

  @Test
  void should_not_find_deleted_user() {
    // Given a persisted user
    var user =
        UserEntity.builder()
            .email("jane.doe@example.com")
            .lastName("Doe")
            .firstName("Jane")
            .password("secret")
            .role(Role.VISUALIZER)
            .build();
    userDao.save(user);
    Long id = user.getId();

    // When the user is deleted (soft delete: is_deleted = true)
    userDao.deleteById(id);
    userDao.flush();

    // Then the user is filtered out by the @SQLRestriction and no longer found
    assertThat(userDao.findByEmail("jane.doe@example.com")).isNull();
    assertThat(userDao.findById(id)).isEmpty();
  }
}
