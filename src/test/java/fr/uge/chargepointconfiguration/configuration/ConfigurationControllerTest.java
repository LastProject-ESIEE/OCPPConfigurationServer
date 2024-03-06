package fr.uge.chargepointconfiguration.configuration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.*;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class ConfigurationControllerTest {
  @Autowired
  private MockMvc mvc;

  @Test
  void getAllConfiguration() {
  }

  @Test
  void getConfigurationById() {
  }

  @Test
  void registerConfiguration() {
  }

  @Test
  void updateConfiguration() {
  }

  @Test
  void getAllConfigurationTranscriptor() {
  }

  @Test
  void getPage() {
  }
}