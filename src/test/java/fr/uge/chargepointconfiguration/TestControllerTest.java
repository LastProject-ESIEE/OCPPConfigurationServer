package fr.uge.chargepointconfiguration;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Package visibility, just for testing purpose.
 */
@Component
class TestController {

  @PreAuthorize("isAuthenticated()") // TODO : should not be necessary
  String authenticated() {
    return "authenticated api";
  }

  @PreAuthorize("hasRole('Administrator')")
  String admin() {
    return "Administrator api";
  }

  @PreAuthorize("hasRole('Editor')")
  String editor() {
    return "Editor api";
  }

  @PreAuthorize("hasRole('Visualizer')")
  String visualizer() {
    return "Visualizer api";
  }
}

@SpringBootTest
@RunWith(SpringRunner.class)
class TestControllerTest {

  @Autowired
  private TestController testController;

  @Test
  @WithAnonymousUser
  void notAuthenticated() {
    assertThrows(AccessDeniedException.class, () -> testController.authenticated());
  }

  @Test
  @WithMockUser
  void authenticated() {
    assertDoesNotThrow(() -> testController.authenticated());
  }

  @Test
  @WithMockUser(roles = "Administrator")
  void authenticatedWithAdminRole() {
    assertDoesNotThrow(() -> testController.authenticated());
  }

  @Test
  @WithMockUser(roles = "RANDOM")
  void adminWithRandomRole() {
    assertThrows(AccessDeniedException.class, () -> testController.admin());
  }

  @Test
  @WithMockUser(roles = "Visualizer")
  void adminWithUserRole() {
    assertThrows(AccessDeniedException.class, () -> testController.admin());
  }

  @Test
  @WithMockUser(roles = "Administrator")
  void adminWithAdministratorRole() {
    assertDoesNotThrow(() -> testController.admin());
  }

  @Test
  @WithMockUser(roles = "RANDOM")
  void editorWithRandomRole() {
    assertThrows(AccessDeniedException.class, () -> testController.editor());
  }

  @Test
  @WithMockUser(roles = "Editor")
  void editorWithEditorRole() {
    assertDoesNotThrow(() -> testController.editor());
  }

  @Test
  @WithMockUser(roles = "Administrator")
  void editorWithAdministratorRole() {
    assertDoesNotThrow(() -> testController.editor());
  }

  @Test
  @WithMockUser(roles = "RANDOM")
  void visualizerWithRandomRole() {
    assertThrows(AccessDeniedException.class, () -> testController.visualizer());
  }

  @Test
  @WithMockUser(roles = "Editor")
  void visualizerWithEditorRole() {
    assertDoesNotThrow(() -> testController.visualizer());
  }

  @Test
  @WithMockUser(roles = "Visualizer")
  void visualizerWithVisualizerRole() {
    assertDoesNotThrow(() -> testController.visualizer());
  }

  @Test
  @WithMockUser(roles = "Administrator")
  void visualizerWithAdministratorRole() {
    assertDoesNotThrow(() -> testController.visualizer());
  }
}