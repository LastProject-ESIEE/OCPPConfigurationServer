package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** Integration tests of the {@code /api/login} endpoint and of the authentication requirement. */
class LoginApiIntegrationTest extends AbstractApiIntegrationTest {

  @Test
  void should_return_token_on_valid_credentials_and_accept_it() throws Exception {
    var token = login("viewer@it");

    mockMvc
        .perform(get("/api/chargepoint/all").header(HttpHeaders.AUTHORIZATION, bearer(token)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void should_return_401_on_wrong_password() throws Exception {
    mockMvc
        .perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"viewer@it\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void should_return_401_when_calling_endpoint_without_token() throws Exception {
    mockMvc.perform(get("/api/chargepoint/all")).andExpect(status().isUnauthorized());
  }
}
