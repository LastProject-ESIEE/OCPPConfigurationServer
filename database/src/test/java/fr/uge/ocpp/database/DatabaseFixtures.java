package fr.uge.ocpp.database;

import fr.uge.ocpp.database.dao.ChargePointDao;
import fr.uge.ocpp.database.dao.ConfigurationDao;
import fr.uge.ocpp.database.dao.FirmwareDao;
import fr.uge.ocpp.database.dao.TypeAllowedDao;
import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.database.entities.ConfigurationEntity;
import fr.uge.ocpp.database.entities.FirmwareEntity;
import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.database.factories.ChargePointEntityFactory;
import fr.uge.ocpp.database.factories.ConfigurationEntityFactory;
import fr.uge.ocpp.database.factories.FirmwareEntityFactory;
import fr.uge.ocpp.database.factories.TypeAllowedEntityFactory;
import java.util.Set;

/**
 * Helpers persisting dummy entities (from the factories) through the real DAOs, in dependency order
 * (type allowed → firmware → configuration → charge point), so tests can seed a database and get
 * back managed entities with generated identifiers.
 */
public final class DatabaseFixtures {

  private DatabaseFixtures() {}

  /** Persists a dummy allowed type and returns it with its generated id. */
  public static TypeAllowedEntity persistTypeAllowed(TypeAllowedDao typeAllowedDao) {
    var entity = TypeAllowedEntityFactory.dummyTypeAllowedEntity();
    entity.setId(null);
    return typeAllowedDao.save(entity);
  }

  /** Persists a dummy firmware ("1.0.0") compatible with the given allowed type. */
  public static FirmwareEntity persistFirmware(
      FirmwareDao firmwareDao, TypeAllowedEntity typeAllowed) {
    return persistFirmware(firmwareDao, "1.0.0", typeAllowed);
  }

  /** Persists a dummy firmware with the given version, compatible with the given allowed type. */
  public static FirmwareEntity persistFirmware(
      FirmwareDao firmwareDao, String version, TypeAllowedEntity typeAllowed) {
    var entity = FirmwareEntityFactory.dummyFirmwareEntityWithoutTypesAllowed();
    entity.setId(null);
    entity.setVersion(version);
    entity.setTypesAllowed(Set.of(typeAllowed));
    return firmwareDao.save(entity);
  }

  /** Persists a whole configuration chain (type allowed, firmware, configuration). */
  public static ConfigurationEntity persistConfiguration(
      ConfigurationDao configurationDao, FirmwareDao firmwareDao, TypeAllowedDao typeAllowedDao) {
    var firmware = persistFirmware(firmwareDao, persistTypeAllowed(typeAllowedDao));
    return persistConfiguration(configurationDao, firmware);
  }

  /** Persists a dummy configuration referencing the given firmware. */
  public static ConfigurationEntity persistConfiguration(
      ConfigurationDao configurationDao, FirmwareEntity firmware) {
    var entity = ConfigurationEntityFactory.dummyConfigurationEntityWithoutFirmware();
    entity.setId(null);
    entity.setFirmware(firmware);
    return configurationDao.save(entity);
  }

  /** Persists a dummy charge point ("client-id") referencing the given configuration. */
  public static ChargePointEntity persistChargePoint(
      ChargePointDao chargePointDao, ConfigurationEntity configuration) {
    return persistChargePoint(chargePointDao, configuration, "client-id");
  }

  /** Persists a dummy charge point with the given client id, referencing the configuration. */
  public static ChargePointEntity persistChargePoint(
      ChargePointDao chargePointDao, ConfigurationEntity configuration, String clientId) {
    var entity = ChargePointEntityFactory.dummyChargePointEntity();
    entity.setId(null);
    entity.setClientId(clientId);
    entity.setConfiguration(configuration);
    return chargePointDao.save(entity);
  }
}
