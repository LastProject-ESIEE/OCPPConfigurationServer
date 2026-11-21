package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.ocpp.database.dao.UserDao;
import fr.uge.ocpp.database.entities.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests of the REST endpoints: full application context, real security chain (JWT
 * login) and real database (in-memory H2 in PostgreSQL compatibility mode).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:ocpp-it;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
      "spring.datasource.driver-class-name=org.h2.Driver",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "websocket.url=127.0.0.1",
      "websocket.port=0"
    })
@AutoConfigureMockMvc
class ApiEndpointsIntegrationTest {

  private static final String PASSWORD = "admin";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UserDao userDao;

  @Autowired private JdbcTemplate jdbcTemplate;

  @Autowired private PasswordEncoder passwordEncoder;

  @BeforeAll
  void seedUsers() {
    // Users are soft-deleted (@SQLDelete on UserEntity), so they are seeded once per class:
    // re-inserting the same emails would violate the unique constraint on the email column.
    saveUser("viewer@it", UserEntity.Role.VISUALIZER);
    saveUser("editor@it", UserEntity.Role.EDITOR);
    saveUser("admin@it", UserEntity.Role.ADMINISTRATOR);
  }

  @BeforeEach
  void cleanDatabase() {
    // Plain SQL deletes in foreign-key order: repository deleteAll() would load the entities and
    // FirmwareEntity.hashCode() fails on the lazy typesAllowed collection outside a session.
    jdbcTemplate.update("DELETE FROM compatibility");
    jdbcTemplate.update("DELETE FROM charge_point");
    jdbcTemplate.update("DELETE FROM configuration");
    jdbcTemplate.update("DELETE FROM firmware");
    jdbcTemplate.update("DELETE FROM type_allowed");
  }

  @Test
  void should_return_401_when_calling_endpoint_without_token() throws Exception {
    mockMvc.perform(get("/api/chargepoint/all")).andExpect(status().isUnauthorized());
  }

  @Test
  void should_return_401_when_login_with_wrong_password() throws Exception {
    mockMvc
        .perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"viewer@it\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void should_login_and_list_charge_points_as_visualizer() throws Exception {
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
  void should_create_resources_and_charge_point_end_to_end_as_editor() throws Exception {
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
    mockMvc
        .perform(get("/api/firmware/all").header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].version").value("6.2.0"));
    mockMvc
        .perform(get("/api/configuration/all").header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].name").value("default"));
    mockMvc
        .perform(get("/api/type/all").header(HttpHeaders.AUTHORIZATION, bearer(viewer)))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(get("/api/type/all").header(HttpHeaders.AUTHORIZATION, bearer(editor)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1));
  }

  @Test
  void should_allow_administrator_to_update_charge_point() throws Exception {
    var admin = login("admin@it");
    var chargePointId = createChargePoint(admin, createConfigurationTree(admin));

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
    createChargePoint(editor, createConfigurationTree(editor));

    mockMvc
        .perform(
            post("/api/chargepoint/create")
                .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                .contentType(MediaType.APPLICATION_JSON)
                .content(chargePointBody("SN-IT-1", "EVE Single S-Line", null)))
        .andExpect(status().isConflict());
  }

  private void saveUser(String email, UserEntity.Role role) {
    userDao.save(
        UserEntity.builder()
            .email(email)
            .lastName("Test")
            .firstName("Test")
            .password(passwordEncoder.encode(PASSWORD))
            .role(role)
            .build());
  }

  private String login(String email) throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"" + email + "\",\"password\":\"" + PASSWORD + "\"}"))
            .andExpect(status().isOk())
            .andReturn();
    return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
  }

  private static String bearer(String token) {
    return "Bearer " + token;
  }

  private long createConfigurationTree(String token) throws Exception {
    var typeId = createAllowedType(token);
    var firmwareId = createFirmware(token, typeId);
    return createConfiguration(token, firmwareId);
  }

  private long createAllowedType(String editor) throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post("/api/type/create")
                    .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"constructor\":\"Alfen\",\"type\":\"EVE Single S-Line\"}"))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  private long createFirmware(String editor, long typeId) throws Exception {
    var body =
        """
        {"url":"ftp://fs.example/6.2.0.fwi","version":"6.2.0","constructor":"Alfen",
         "typesAllowed":[{"id":%d,"constructor":"Alfen","type":"EVE Single S-Line"}]}
        """
            .formatted(typeId);
    MvcResult result =
        mockMvc
            .perform(
                post("/api/firmware/create")
                    .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  private long createConfiguration(String editor, long firmwareId) throws Exception {
    var body =
        """
        {"name":"default","description":"d","configuration":"{\\"1\\":\\"80\\"}","firmware":%d}
        """
            .formatted(firmwareId);
    MvcResult result =
        mockMvc
            .perform(
                post("/api/configuration/create")
                    .header(HttpHeaders.AUTHORIZATION, bearer(editor))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  private long createChargePoint(String token, Long configurationId) throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post("/api/chargepoint/create")
                    .header(HttpHeaders.AUTHORIZATION, bearer(token))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(chargePointBody("SN-IT-1", "EVE Single S-Line", configurationId)))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  private static String chargePointBody(String serialNumber, String type, Long configurationId) {
    var configuration = configurationId == null ? "" : ",\"configuration\":" + configurationId;
    return """
        {"serialNumber":"%s","type":"%s","constructor":"Alfen","clientId":"client-1"%s}
        """
        .formatted(serialNumber, type, configuration);
  }

  private long readId(MvcResult result) throws Exception {
    return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
  }
}
