package fr.uge.ocpp.domain.ports.api;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;

public interface ConfigurationService {

  Configuration save(Configuration configuration);

  Configuration update(long id, Configuration configuration);

  Configurations findAll();

  Configuration findById(long id);

  long countTotalWithFilter(String request);

  Configurations search(String request, PageQuery pageQuery);

}
