package fr.uge.chargepointconfiguration.logs.technical;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.*;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class TechnicalLogControllerTest {
  @Autowired
  private MockMvc mvc;

  @Test
  void getTechnicalLogByComponentAndLevel() {
  }

  @Test
  void getPage() {
  }
}