package fr.uge.ocpp.service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private JwtService jwtService;

  @MockBean private AuthenticationManager authenticationManager;

  @TestConfiguration
  static class JwtServiceConfig {
    @Bean
    JwtService jwtService() {
      return new JwtService("test-secret-key-that-is-long-enough-for-hs256", 60_000);
    }
  }

  @Test
  void should_return_jwt_on_valid_credentials() throws Exception {
    when(authenticationManager.authenticate(any()))
        .thenReturn(new UsernamePasswordAuthenticationToken("dummy@local", null, List.of()));

    var result =
        mockMvc
            .perform(
                post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"dummy@local\",\"password\":\"admin\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isString())
            .andReturn();

    var token = objectMapper.readTree(result.getResponse().getContentAsString()).get("token");
    assertEquals("dummy@local", jwtService.extractEmail(token.asText()));
  }

  @Test
  void should_return_401_on_bad_credentials() throws Exception {
    when(authenticationManager.authenticate(any()))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    mockMvc
        .perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"dummy@local\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized());
  }
}
