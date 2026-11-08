package fr.uge.ocpp.database.adapters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.uge.ocpp.database.dao.ChargePointDao;
import fr.uge.ocpp.database.dao.ConfigurationDao;
import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.database.entities.ConfigurationEntity;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Sort;
import fr.uge.ocpp.domain.models.StatusProcess;
import fr.uge.ocpp.domain.models.Step;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ChargePointRepositoryAdapterTest {

  private final ChargePointDao chargePointDao = mock(ChargePointDao.class);
  private final ConfigurationDao configurationDao = mock(ConfigurationDao.class);
  private final ChargePointRepositoryAdapter adapter =
      new ChargePointRepositoryAdapter(chargePointDao);

  @Test
  void should_save_and_map_domain_model_to_entity() {
    var configurationEntity =
        ConfigurationEntity.builder()
            .name("config")
            .description("desc")
            .configuration("{}")
            .build();
    configurationEntity.setId(7L);
    when(configurationDao.findById(7L)).thenReturn(Optional.of(configurationEntity));
    var savedEntity =
        ChargePointEntity.builder()
            .serialNumberChargepoint("serial")
            .type("Type")
            .constructor("Const")
            .clientId("client-id")
            .configuration(configurationEntity)
            .build();
    savedEntity.setId(3L);
    when(chargePointDao.save(any(ChargePointEntity.class))).thenReturn(savedEntity);
    var firmware = new fr.uge.ocpp.domain.models.Firmware(9L, "url", "1.0.0", "Const", null);
    var configuration = new Configuration(7L, "config", "desc", null, "{}", firmware);
    var chargePoint =
        new ChargePoint(
            null,
            "serial",
            "Type",
            "Const",
            "client-id",
            null,
            "",
            false,
            Step.FIRMWARE,
            StatusProcess.PENDING,
            configuration);

    ChargePoint saved = adapter.save(chargePoint);

    var entityCaptor = ArgumentCaptor.forClass(ChargePointEntity.class);
    verify(chargePointDao).save(entityCaptor.capture());
    assertThat(entityCaptor.getValue().getSerialNumberChargepoint()).isEqualTo("serial");
    assertThat(entityCaptor.getValue().getConfiguration().getId()).isEqualTo(7L);
    assertThat(saved.id()).isEqualTo(3L);
    assertThat(saved.configuration().name()).isEqualTo("config");
  }

  @Test
  void should_find_by_id_and_map_entity_to_domain_model() {
    var configurationEntity =
        ConfigurationEntity.builder()
            .name("config")
            .description("desc")
            .configuration("{}")
            .build();
    configurationEntity.setId(7L);
    var entity =
        ChargePointEntity.builder()
            .serialNumberChargepoint("serial")
            .type("Type")
            .constructor("Const")
            .clientId("client-id")
            .configuration(configurationEntity)
            .build();
    entity.setId(3L);
    when(chargePointDao.findById(3L)).thenReturn(Optional.of(entity));

    Optional<ChargePoint> found = adapter.findById(3L);

    assertThat(found).isPresent();
    assertThat(found.get().clientId()).isEqualTo("client-id");
    assertThat(found.get().configuration().id()).isEqualTo(7L);
  }

  @Test
  void should_map_page_query_sort_direction() {
    var pageableCaptor =
        org.mockito.ArgumentCaptor.forClass(org.springframework.data.domain.Pageable.class);
    when(chargePointDao.findAllByClientIdContainingIgnoreCaseOrderByIdDesc(any(), any()))
        .thenReturn(org.springframework.data.domain.Page.empty());
    var pageQuery = new fr.uge.ocpp.domain.models.PageQuery(2, 10, Sort.ASC);

    adapter.findAllByClientIdContainingIgnoreCaseOrderByIdDesc(pageQuery, "client");

    verify(chargePointDao)
        .findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
            pageableCaptor.capture(), org.mockito.ArgumentMatchers.eq("client"));
    assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
    assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
  }
}
