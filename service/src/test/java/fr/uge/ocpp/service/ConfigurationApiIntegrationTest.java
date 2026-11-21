package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** Integration tests of the {@code /api/configuration} endpoints. */
class ConfigurationApiIntegrationTest extends AbstractApiIntegrationTest {

  @Test
  void should_create_and_list_configuration() throws Exception {
    var editor = login("editor@it");
    var typeId = createAllowedType(editor);
    var firmwareId = createFirmware(editor, typeId);
    var configurationId = createConfiguration(editor, firmwareId);

    var viewer = login("viewer@it");
    mockMvc
        .perform(get("/api/configuration/all").header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].name").value("default"));
    mockMvc
        .perform(
            get("/api/configuration/{id}", configurationId)
                .header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("default"));
  }

  @Test
  void should_return_404_when_firmware_unknown_on_create() throws Exception {
    var editor = login("editor@it");

    mockMvc
        .perform(
            post("/api/configuration/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\":\"x\",\"description\":\"d\",\"configuration\":\"{}\",\"firmware\":9999}"))
        .andExpect(status().isNotFound());
  }
}
