package fr.uge.chargepointconfiguration.logs.business;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BusinessLogControllerTest {
  @Autowired
  private MockMvc mvc;

  @Test
  @Disabled
  void getBusinessLogByChargepointId() {
  }


  @Test
  void getPage() throws Exception {
    mvc.perform(get("/api/log/business/search")
            .queryParam("size", "2")
            .queryParam("page", "0")
            .queryParam("sortBy", "date")
            .queryParam("order", "desc")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.total", is(4)))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(2)))
        .andExpect(jsonPath("$.data", hasSize(2)))
        .andExpect(jsonPath("$.data[0].date", is("2024-03-08T12:00:00.000+00:00")))
        .andExpect(jsonPath("$.data[1].date", is("2024-03-08T11:00:00.000+00:00")));

    mvc.perform(get("/api/log/business/search")
            .queryParam("size", "2")
            .queryParam("page", "1")
            .queryParam("sortBy", "date")
            .queryParam("order", "desc")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.total", is(4)))
        .andExpect(jsonPath("$.page", is(1)))
        .andExpect(jsonPath("$.size", is(2)))
        .andExpect(jsonPath("$.data", hasSize(2)))
        .andExpect(jsonPath("$.data[0].date", is("2024-03-08T10:00:00.000+00:00")))
        .andExpect(jsonPath("$.data[1].date", is("2024-03-08T09:00:00.000+00:00")));
  }

  @Disabled
  @Test
  void getPageWithFilterMultiple() throws Exception {
    mvc.perform(get("/api/log/business/search")
            .queryParam("size", "2")
            .queryParam("page", "0")
            .queryParam("sortBy", "clientId")
            .queryParam("order", "desc")
            .queryParam("request", "clientId:`les`,type:`Single`")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.total", is(8)))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(2)))
        .andExpect(jsonPath("$.data", hasSize(1)))
        .andExpect(jsonPath("$.data[0].clientId", is("les bornés")))
        .andExpect(jsonPath("$.data[0].type", is("Eve Single S-line")));
  }

  @Disabled
  @Test
  void getPageWithFilter() throws Exception {
    mvc.perform(get("/api/log/business/search")
            .queryParam("size", "2")
            .queryParam("page", "0")
            .queryParam("sortBy", "clientId")
            .queryParam("order", "desc")
            .queryParam("request", "clientId:`borne`")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.total", is(8)))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(2)))
        .andExpect(jsonPath("$.data", hasSize(2)))
        .andExpect(jsonPath("$.data[0].clientId", is("stéphane borne (l'historien)")))
        .andExpect(jsonPath("$.data[1].clientId", is("nom de la borne")));
  }
}