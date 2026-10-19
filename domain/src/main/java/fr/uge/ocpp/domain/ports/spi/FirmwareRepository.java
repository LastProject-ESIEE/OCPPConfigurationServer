package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;

public interface FirmwareRepository {

  Firmware findByVersion(String version);

  Firmwares findAllByTypeAllowedAsc(TypeAllowed typeAllowed);

  Firmwares findAllByTypeAllowedDesc(TypeAllowed typeAllowed);
  Firmwares findAllByOrderByIdDesc();

  PageResult<Firmware> findAllByOrderByIdDesc(PageQuery pageQuery);

  Firmware findByUrl(String url);
}
