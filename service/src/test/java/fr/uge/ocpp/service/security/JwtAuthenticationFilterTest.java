package fr.uge.ocpp.service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.uge.ocpp.database.entities.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtAuthenticationFilterTest {

  private static final String SECRET = "test-secret-key-that-is-long-enough-for-hs256";

  private final JwtService jwtService = new JwtService(SECRET, 60_000);
  private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
  private final JwtAuthenticationFilter filter =
      new JwtAuthenticationFilter(jwtService, userDetailsService);

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void should_authenticate_request_with_valid_bearer_token() throws Exception {
    var user =
        new UserEntity("Dummy", "Dummy", "dummy@local", "encoded", UserEntity.Role.VISUALIZER);
    when(userDetailsService.loadUserByUsername("dummy@local"))
        .thenReturn(new UserDetailsImpl(user));
    var request = new MockHttpServletRequest();
    request.addHeader(
        HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateToken("dummy@local"));
    var chain = new MockFilterChain();

    filter.doFilter(request, new MockHttpServletResponse(), chain);

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(authentication);
    assertEquals("dummy@local", authentication.getName());
    assertTrue(
        authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_VISUALIZER")));
    assertNotNull(chain.getRequest());
  }

  @Test
  void should_leave_request_unauthenticated_without_header() throws Exception {
    var request = new MockHttpServletRequest();
    var chain = new MockFilterChain();

    filter.doFilter(request, new MockHttpServletResponse(), chain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    assertNotNull(chain.getRequest());
  }

  @Test
  void should_leave_request_unauthenticated_with_invalid_token() throws Exception {
    var request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer not-a-jwt");
    var chain = new MockFilterChain();

    filter.doFilter(request, new MockHttpServletResponse(), chain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    assertNotNull(chain.getRequest());
  }
}
