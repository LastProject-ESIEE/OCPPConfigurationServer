package fr.uge.chargepointconfiguration.logs.technical;

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
class TechnicalLogControllerTest {
  @Autowired
  private MockMvc mvc;

  @Test
  @Disabled
  void getTechnicalLogByComponentAndLevel() {
  }

  @Test
  @Disabled
  void getPage() {
  }
}