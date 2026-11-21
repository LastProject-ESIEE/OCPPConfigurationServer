package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/** Integration tests of the {@code /api/chargepoint} endpoints. */
class ChargePointApiIntegrationTest extends AbstractApiIntegrationTest {

  @Test
  void should_list_charge_points_as_visualizer() throws Exception {
    var token = login("viewer@it");

    mockMvc
        .perform(get("/api/chargepoint/all").header(HttpHeaders.AUTHORIZATION, bearer(token)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.totalElements").value(0));
  }

  @Test
  void should_return_404_when_charge_point_does_not_exist() throws Exception {
    var token = login("viewer@it");

    mockMvc
        .perform(
            get("/api/chargepoint/{id}", 424242L).header(HttpHeaders.AUTHORIZATION, bearer(token)))
        .andExpect(status().isNotFound());
  }

  @Test
  void should_forbid_visualizer_to_create_charge_point() throws Exception {
    var token = login("viewer@it");

    mockMvc
        .perform(
            post("/api/chargepoint/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(chargePointBody("SN-IT-1", "EVE Single S-Line", null)))
        .andExpect(status().isForbidden());
  }

  @Test
  void should_create_and_read_charge_point_end_to_end_as_editor() throws Exception {
    var editor = login("editor@it");
    var typeId = createAllowedType(editor);
    var firmwareId = createFirmware(editor, typeId);
    var configurationId = createConfiguration(editor, firmwareId);
    var chargePointId = createChargePoint(editor, configurationId);

    var viewer = login("viewer@it");
    mockMvc
        .perform(get("/api/chargepoint/all").header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content[0].serialNumberChargepoint").value("SN-IT-1"));
    mockMvc
        .perform(
            get("/api/chargepoint/{id}", chargePointId)
                .header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.clientId").value("client-1"));
  }

  @Test
  void should_allow_administrator_to_update_charge_point() throws Exception {
    var admin = login("admin@it");
    var typeId = createAllowedType(admin);
    var firmwareId = createFirmware(admin, typeId);
    var configurationId = createConfiguration(admin, firmwareId);
    var chargePointId = createChargePoint(admin, configurationId);

    mockMvc
        .perform(
            patch("/api/chargepoint/{id}", chargePointId)
                .header(HttpHeaders.AUTHORIZATION, bearer(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(chargePointBody("SN-IT-1", "NEW-TYPE", null)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type").value("NEW-TYPE"));
  }

  @Test
  void should_return_409_when_serial_number_already_exists() throws Exception {
    var editor = login("editor@it");
    var typeId = createAllowedType(editor);
    var firmwareId = createFirmware(editor, typeId);
    var configurationId = createConfiguration(editor, firmwareId);
    createChargePoint(editor, configurationId);

    mockMvc
        .perform(
            post("/api/chargepoint/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                .contentType(MediaType.APPLICATION_JSON)
                .content(chargePointBody("SN-IT-1", "EVE Single S-Line", configurationId)))
        .andExpect(status().isConflict());
  }
}
