package fr.uge.ocpp.service.chargepoint;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.ocpp.domain.exceptions.ChargePointNotFoundException;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChargePointController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChargePointControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ChargePointService chargePointService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void should_return_charge_point_by_id() throws Exception {
    var chargePoint = new ChargePoint(1L, "serial", "Type", "Const", "client-id", null, "",
        false, Step.FIRMWARE, StatusProcess.PENDING, null);
    when(chargePointService.findById(1L)).thenReturn(chargePoint);

    mockMvc.perform(get("/api/chargepoint/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("serialNumberChargepoint").value("serial"))
        .andExpect(jsonPath("clientId").value("client-id"))
        .andExpect(jsonPath("status.step").value("FIRMWARE"));
  }

  @Test
  void should_return_404_when_charge_point_not_found() throws Exception {
    when(chargePointService.findById(42L)).thenThrow(new ChargePointNotFoundException("not found"));

    mockMvc.perform(get("/api/chargepoint/{id}", 42L))
        .andExpect(status().isNotFound());
  }

  @Test
  void should_create_charge_point() throws Exception {
    var chargePoint = new ChargePoint(1L, "serial", "Type", "Const", "client-id", null, "",
        false, Step.FIRMWARE, StatusProcess.PENDING, null);
    when(chargePointService.save(any(ChargePoint.class))).thenReturn(chargePoint);
    var body = objectMapper.writeValueAsString(
        new CreateChargePointDto("serial", "Type", "Const", "client-id", null));

    mockMvc.perform(post("/api/chargepoint/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("type").value("Type"));
  }

  @Test
  void should_update_charge_point() throws Exception {
    var chargePoint = new ChargePoint(1L, "serial", "NEW_TYPE", "Const", "client-id", null, "",
        false, Step.FIRMWARE, StatusProcess.PENDING, null);
    when(chargePointService.update(anyLong(), any(ChargePoint.class))).thenReturn(chargePoint);
    var body = objectMapper.writeValueAsString(
        new CreateChargePointDto("serial", "NEW_TYPE", "Const", "client-id", -1L));

    mockMvc.perform(patch("/api/chargepoint/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("type").value("NEW_TYPE"));
  }
}
