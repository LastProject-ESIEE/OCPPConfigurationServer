package fr.uge.ocpp.service.security;

import static fr.uge.ocpp.domain.models.ChargePointFactory.dummyChargePointWithoutConfiguration;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.ocpp.database.dao.UserDao;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
import fr.uge.ocpp.service.chargepoint.ChargePointController;
import fr.uge.ocpp.service.chargepoint.CreateChargePointDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChargePointController.class)
@Import(WebSecurityConfig.class)
@AutoConfigureMockMvc
class EndpointAuthorizationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ChargePointService chargePointService;

  @MockBean private UserDao userDao;

  @Test
  void should_return_401_when_not_authenticated() throws Exception {
    mockMvc.perform(get("/api/chargepoint/all")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "VISUALIZER")
  void should_allow_visualizer_to_read_charge_points() throws Exception {
    when(chargePointService.findAll(any(), any()))
        .thenReturn(new PageResult<>(List.of(dummyChargePointWithoutConfiguration()), 0, 20, 1));

    mockMvc.perform(get("/api/chargepoint/all")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void should_allow_editor_to_read_charge_points() throws Exception {
    when(chargePointService.findAll(any(), any()))
        .thenReturn(new PageResult<>(List.of(dummyChargePointWithoutConfiguration()), 0, 20, 1));

    mockMvc.perform(get("/api/chargepoint/all")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMINISTRATOR")
  void should_allow_administrator_to_read_charge_points() throws Exception {
    when(chargePointService.findAll(any(), any()))
        .thenReturn(new PageResult<>(List.of(dummyChargePointWithoutConfiguration()), 0, 20, 1));

    mockMvc.perform(get("/api/chargepoint/all")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "VISUALIZER")
  void should_forbid_visualizer_to_create_charge_point() throws Exception {
    var body =
        objectMapper.writeValueAsString(
            new CreateChargePointDto("serial", "Type", "Const", "client-id", null));

    mockMvc
        .perform(
            post("/api/chargepoint/create").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "EDITOR")
  void should_allow_editor_to_create_charge_point() throws Exception {
    when(chargePointService.save(any(ChargePoint.class)))
        .thenReturn(dummyChargePointWithoutConfiguration());
    var body =
        objectMapper.writeValueAsString(
            new CreateChargePointDto("serial", "Type", "Const", "client-id", null));

    mockMvc
        .perform(
            post("/api/chargepoint/create").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "ADMINISTRATOR")
  void should_allow_administrator_to_create_charge_point() throws Exception {
    when(chargePointService.save(any(ChargePoint.class)))
        .thenReturn(dummyChargePointWithoutConfiguration());
    var body =
        objectMapper.writeValueAsString(
            new CreateChargePointDto("serial", "Type", "Const", "client-id", null));

    mockMvc
        .perform(
            post("/api/chargepoint/create").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated());
  }
}
