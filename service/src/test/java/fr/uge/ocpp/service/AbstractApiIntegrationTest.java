package fr.uge.ocpp.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.ocpp.database.dao.UserDao;
import fr.uge.ocpp.database.entities.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class of the REST endpoints integration tests. Boots the full application (in-memory H2 in
 * PostgreSQL compatibility mode, OCPP web socket server on an ephemeral port) and provides helpers
 * to log in and call the endpoints with a real JWT bearer token.
 *
 * <p>Each test runs in a transaction that is rolled back, so the database stays clean between
 * tests. Users (one per role) are seeded once per class in {@link #seedUsers()}, because {@code
 * UserEntity} is soft-deleted and re-inserting the same email would violate the unique constraint.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
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
abstract class AbstractApiIntegrationTest {

  protected static final String PASSWORD = "admin";

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @Autowired private UserDao userDao;

  @Autowired private PasswordEncoder passwordEncoder;

  @BeforeAll
  void seedUsers() {
    saveUser("viewer@it", UserEntity.Role.VISUALIZER);
    saveUser("editor@it", UserEntity.Role.EDITOR);
    saveUser("admin@it", UserEntity.Role.ADMINISTRATOR);
  }

  protected String login(String email) throws Exception {
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

  protected static String bearer(String token) {
    return "Bearer " + token;
  }

  protected long readId(MvcResult result) throws Exception {
    return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
  }

  protected long createAllowedType(String token) throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post("/api/type/create")
                    .header(HttpHeaders.AUTHORIZATION, bearer(token))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"constructor\":\"Alfen\",\"type\":\"EVE Single S-Line\"}"))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  protected long createFirmware(String token, long typeId) throws Exception {
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
                    .header(HttpHeaders.AUTHORIZATION, bearer(token))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  protected long createConfiguration(String token, long firmwareId) throws Exception {
    var body =
        """
        {"name":"default","description":"d","configuration":"{\\"1\\":\\"80\\"}","firmware":%d}
        """
            .formatted(firmwareId);
    MvcResult result =
        mockMvc
            .perform(
                post("/api/configuration/create")
                    .header(HttpHeaders.AUTHORIZATION, bearer(token))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
            .andExpect(status().isCreated())
            .andReturn();
    return readId(result);
  }

  protected long createChargePoint(String token, Long configurationId) throws Exception {
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

  protected static String chargePointBody(String serialNumber, String type, Long configurationId) {
    var configuration = configurationId == null ? "" : ",\"configuration\":" + configurationId;
    return """
        {"serialNumber":"%s","type":"%s","constructor":"Alfen","clientId":"client-1"%s}
        """
        .formatted(serialNumber, type, configuration);
  }

  private void saveUser(String email, UserEntity.Role role) {
    if (userDao.findByEmail(email) != null) {
      return;
    }
    userDao.save(
        UserEntity.builder()
            .email(email)
            .lastName("Test")
            .firstName("Test")
            .password(passwordEncoder.encode(PASSWORD))
            .role(role)
            .build());
  }
}
