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
        .andExpect(jsonPath("$.data[0].id", is(1)))
        .andExpect(jsonPath("$.data[1].id", is(3)));

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
        .andExpect(jsonPath("$.data[0].id", is(2)))
        .andExpect(jsonPath("$.data[1].id", is(4)));
  }

  @Test
  void getPageWithFilterMultiple() throws Exception {
    mvc.perform(get("/api/log/business/search")
            .queryParam("size", "10")
            .queryParam("page", "0")
            .queryParam("sortBy", "date")
            .queryParam("order", "desc")
            .queryParam("request", "date>`2024-03-08T10:01:00.00`,date<`2024-03-08T11:00:00.00`")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.total", is(4)))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(10)))
        .andExpect(jsonPath("$.data", hasSize(1)))
        .andExpect(jsonPath("$.data[0].id", is(2)));
  }

  @Test
  void getPageWithFilter() throws Exception {
    mvc.perform(get("/api/log/business/search")
            .queryParam("size", "10")
            .queryParam("page", "0")
            .queryParam("sortBy", "date")
            .queryParam("order", "desc")
            .queryParam("request", "date>`2024-03-08T10:01:00.00`")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.total", is(4)))
        .andExpect(jsonPath("$.page", is(0)))
        .andExpect(jsonPath("$.size", is(10)))
        .andExpect(jsonPath("$.data", hasSize(3)))
        .andExpect(jsonPath("$.data[0].id", is(1)))
        .andExpect(jsonPath("$.data[1].id", is(3)))
        .andExpect(jsonPath("$.data[2].id", is(2)));
  }
}