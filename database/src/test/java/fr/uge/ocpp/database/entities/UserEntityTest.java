package fr.uge.ocpp.database.entities;

import static org.assertj.core.api.Assertions.assertThat;

import fr.uge.ocpp.database.entities.UserEntity.Role;
import org.junit.jupiter.api.Test;

class UserEntityTest {

  @Test
  void should_build_user_with_role() {
    // Given the fields of a user
    // When a user entity is built
    var user =
        UserEntity.builder()
            .id(1L)
            .email("john.doe@example.com")
            .lastName("Doe")
            .firstName("John")
            .password("secret")
            .role(Role.ADMINISTRATOR)
            .build();

    // Then every field is set and the user is not deleted by default
    assertThat(user.getId()).isEqualTo(1L);
    assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    assertThat(user.getLastName()).isEqualTo("Doe");
    assertThat(user.getFirstName()).isEqualTo("John");
    assertThat(user.getPassword()).isEqualTo("secret");
    assertThat(user.getRole()).isEqualTo(Role.ADMINISTRATOR);
    assertThat(user.isDeleted()).isFalse();
  }

  @Test
  void should_expose_all_roles() {
    // Given the role enum
    // When all the roles are listed
    Role[] roles = Role.values();

    // Then the three roles are available and can be resolved by name
    assertThat(roles).containsExactly(Role.VISUALIZER, Role.EDITOR, Role.ADMINISTRATOR);
    assertThat(Role.valueOf("EDITOR")).isEqualTo(Role.EDITOR);
  }
}
