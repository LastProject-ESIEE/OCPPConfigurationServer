package fr.uge.chargepointconfiguration.chargepoint;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.*;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class ChargepointControllerTest {
  @Autowired
  private MockMvc mvc;

  @Test
  void getAllChargepoints() {
  }

  @Test
  void getChargepointById() {
  }

  @Test
  void searchChargepoints() {
  }

  @Test
  void registerChargepoint() {
  }

  @Test
  void updateChargepoint() {
  }
}