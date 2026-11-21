package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** Integration tests of the {@code /api/type} endpoints. */
class TypeAllowedApiIntegrationTest extends AbstractApiIntegrationTest {

  @Test
  void should_create_and_list_allowed_type_as_editor() throws Exception {
    var editor = login("editor@it");

    mockMvc
        .perform(
            post("/api/type/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"constructor\":\"Alfen\",\"type\":\"EVE Single S-Line\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.constructor").value("Alfen"));
    mockMvc
        .perform(get("/api/type/all").header(HttpHeaders.AUTHORIZATION, bearer(editor)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void should_forbid_visualizer_to_create_allowed_type() throws Exception {
    var token = login("viewer@it");

    mockMvc
        .perform(
            post("/api/type/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"constructor\":\"Alfen\",\"type\":\"EVE Single S-Line\"}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void should_forbid_visualizer_to_list_allowed_types() throws Exception {
    var token = login("viewer@it");

    mockMvc
        .perform(get("/api/type/all").header(HttpHeaders.AUTHORIZATION, bearer(token)))
        .andExpect(status().isForbidden());
  }
}
