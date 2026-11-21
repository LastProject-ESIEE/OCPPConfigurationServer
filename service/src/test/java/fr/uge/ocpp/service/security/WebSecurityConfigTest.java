package fr.uge.ocpp.service.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class WebSecurityConfigTest {

  @Test
  void should_make_administrator_reach_editor_and_visualizer_authorities() {
    var hierarchy = WebSecurityConfig.roleHierarchy();

    var reachable =
        hierarchy.getReachableGrantedAuthorities(
            List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR")));

    assertThat(reachable)
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder("ROLE_ADMINISTRATOR", "ROLE_EDITOR", "ROLE_VISUALIZER");
  }

  @Test
  void should_make_editor_reach_visualizer_authority() {
    var hierarchy = WebSecurityConfig.roleHierarchy();

    var reachable =
        hierarchy.getReachableGrantedAuthorities(
            List.of(new SimpleGrantedAuthority("ROLE_EDITOR")));

    assertThat(reachable)
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder("ROLE_EDITOR", "ROLE_VISUALIZER");
  }

  @Test
  void should_not_make_visualizer_reach_higher_authorities() {
    var hierarchy = WebSecurityConfig.roleHierarchy();

    var reachable =
        hierarchy.getReachableGrantedAuthorities(
            List.of(new SimpleGrantedAuthority("ROLE_VISUALIZER")));

    assertThat(reachable)
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly("ROLE_VISUALIZER");
  }
}
