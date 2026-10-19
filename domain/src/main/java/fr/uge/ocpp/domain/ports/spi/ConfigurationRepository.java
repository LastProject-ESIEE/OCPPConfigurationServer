package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;

public interface ConfigurationRepository {

  Configurations findAllByOrderByIdDesc();
  PageResult<Configuration> findAllByOrderByIdDesc(PageQuery pageQuery);
}
