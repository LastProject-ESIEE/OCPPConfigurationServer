package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;

import java.util.Optional;

public interface FirmwareRepository {

  Optional<Firmware> findByVersion(String version);

  Firmwares findAllByTypeAllowedAsc(TypeAllowed typeAllowed);

  Firmwares findAllByTypeAllowedDesc(TypeAllowed typeAllowed);

  Firmwares findAllByOrderByIdDesc();

  PageResult<Firmware> findAllByOrderByIdDesc(PageQuery pageQuery);

  Optional<Firmware> findByUrl(String url);

  Optional<Firmware> findById(long id);
}
