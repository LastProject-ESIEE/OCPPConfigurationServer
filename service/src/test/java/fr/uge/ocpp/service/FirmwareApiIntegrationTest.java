package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** Integration tests of the {@code /api/firmware} endpoints. */
class FirmwareApiIntegrationTest extends AbstractApiIntegrationTest {

  @Test
  void should_create_and_read_firmware_as_editor_and_visualizer() throws Exception {
    var editor = login("editor@it");
    var typeId = createAllowedType(editor);
    var firmwareId = createFirmware(editor, typeId);

    var viewer = login("viewer@it");
    mockMvc
        .perform(get("/api/firmware/all").header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].version").value("6.2.0"));
    mockMvc
        .perform(
            get("/api/firmware/{id}", firmwareId).header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.url").value("ftp://fs.example/6.2.0.fwi"));
  }

  @Test
  void should_return_404_when_allowed_type_unknown_on_create() throws Exception {
    var editor = login("editor@it");
    var body =
        """
        {"url":"ftp://fs.example/9.9.9.fwi","version":"9.9.9","constructor":"Alfen",
         "typesAllowed":[{"id":9999,"constructor":"Alfen","type":"Unknown"}]}
        """;

    mockMvc
        .perform(
            post("/api/firmware/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isNotFound());
  }
}
