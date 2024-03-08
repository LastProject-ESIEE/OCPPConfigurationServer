package fr.uge.chargepointconfiguration.firmware;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FirmwareControllerTest {
  @Autowired
  private MockMvc mvc;

  @Test
  @Disabled
  void getAllFirmwares() {
  }

  @Test
  @Disabled
  void getFirmwareById() {
  }

  @Test
  @Disabled
  void getPage() {
  }
}