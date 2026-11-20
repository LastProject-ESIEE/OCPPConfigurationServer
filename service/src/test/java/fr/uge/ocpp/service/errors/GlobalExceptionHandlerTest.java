package fr.uge.ocpp.service.errors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.uge.ocpp.domain.exceptions.ChargePointNotFoundException;
import fr.uge.ocpp.domain.ports.api.ChargePointService;
import fr.uge.ocpp.service.chargepoint.ChargePointController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChargePointController.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ChargePointService chargePointService;

  @Test
  void should_return_404_with_error_message_when_domain_not_found() throws Exception {
    // given
    when(chargePointService.findById(42L)).thenThrow(new ChargePointNotFoundException("not found"));

    // when
    var result = mockMvc.perform(get("/api/chargepoint/{id}", 42L));

    // then
    result.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("not found"));
  }

  @Test
  void should_return_404_with_error_message_when_route_unknown() throws Exception {
    // given: no stubbing, the route does not exist

    // when
    var result = mockMvc.perform(get("/api/does-not-exist"));

    // then
    result.andExpect(status().isNotFound()).andExpect(jsonPath("$.message").exists());
  }
}
