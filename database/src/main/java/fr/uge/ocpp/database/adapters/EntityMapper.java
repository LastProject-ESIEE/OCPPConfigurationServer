package fr.uge.ocpp.database.adapters;

import fr.uge.ocpp.database.entities.ChargePointEntity;
import fr.uge.ocpp.database.entities.ConfigurationEntity;
import fr.uge.ocpp.database.entities.FirmwareEntity;
import fr.uge.ocpp.database.entities.TypeAllowedEntity;
import fr.uge.ocpp.database.repositories.ConfigurationJpaRepository;
import fr.uge.ocpp.database.repositories.FirmwareJpaRepository;
import fr.uge.ocpp.database.repositories.TypeAllowedJpaRepository;
import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.models.TypesAllowed;
import java.util.Set;
import java.util.stream.Collectors;

/** Maps the JPA entities to the domain models and back. */
final class EntityMapper {

  /** Creates a new mapper, this class should not be instantiated. */
  private EntityMapper() {}

  /**
   * Maps an allowed type entity to the corresponding domain model.
   *
   * @param entity the entity to map.
   * @return the corresponding domain model.
   */
  static TypeAllowed toTypeAllowed(TypeAllowedEntity entity) {
    return new TypeAllowed(entity.getId(), entity.getConstructor(), entity.getType());
  }

  /**
   * Maps an allowed type domain model to the corresponding entity reference.
   *
   * @param typeAllowed the domain model to map.
   * @return the corresponding entity reference.
   */
  static TypeAllowedEntity toTypeAllowedEntity(TypeAllowed typeAllowed) {
    return TypeAllowedEntity.reference(typeAllowed.id());
  }

  /**
   * Maps a firmware entity to the corresponding domain model.
   *
   * @param entity the entity to map.
   * @return the corresponding domain model.
   */
  static Firmware toFirmware(FirmwareEntity entity) {
    var typesAllowed =
        entity.getTypesAllowed().stream()
            .map(EntityMapper::toTypeAllowed)
            .collect(Collectors.toSet());
    return new Firmware(
        entity.getId(),
        entity.getUrl(),
        entity.getVersion(),
        entity.getConstructor(),
        new TypesAllowed(typesAllowed));
  }

  /**
   * Maps a firmware domain model to the corresponding entity reference.
   *
   * @param firmware the domain model to map.
   * @return the corresponding entity reference.
   */
  static FirmwareEntity toFirmwareReference(Firmware firmware) {
    if (firmware == null) {
      return null;
    }
    return FirmwareEntity.reference(firmware.id());
  }

  /**
   * Maps a firmware domain model to the corresponding entity.
   *
   * @param firmware the domain model to map.
   * @param repository the repository used to resolve the allowed types.
   * @return the corresponding entity.
   */
  static FirmwareEntity toFirmwareEntity(Firmware firmware, TypeAllowedJpaRepository repository) {
    var typesAllowed =
        firmware.typesAllowed().typesAllowedSet().stream()
            .map(typeAllowed -> repository.findById(typeAllowed.id()).orElseThrow())
            .collect(Collectors.toSet());
    var entity =
        new FirmwareEntity(
            firmware.url(), firmware.version(), firmware.constructor(), typesAllowed);
    entity.setId(firmware.id());
    return entity;
  }

  /**
   * Maps a configuration entity to the corresponding domain model.
   *
   * @param entity the entity to map.
   * @return the corresponding domain model.
   */
  static Configuration toConfiguration(ConfigurationEntity entity) {
    var firmware = entity.getFirmware() == null ? null : toFirmware(entity.getFirmware());
    return new Configuration(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getLastEdit(),
        entity.getConfiguration(),
        firmware);
  }

  /**
   * Maps a configuration domain model to the corresponding entity.
   *
   * @param configuration the domain model to map.
   * @param firmwareRepository the repository used to resolve the firmware.
   * @return the corresponding entity.
   */
  static ConfigurationEntity toConfigurationEntity(
      Configuration configuration, FirmwareJpaRepository firmwareRepository) {
    FirmwareEntity firmware = null;
    if (configuration.firmware() != null) {
      firmware =
          firmwareRepository
              .findById(configuration.firmware().id())
              .orElseGet(() -> toFirmwareReference(configuration.firmware()));
    }
    var entity =
        new ConfigurationEntity(
            configuration.name(),
            configuration.description(),
            configuration.configuration(),
            firmware);
    entity.setId(configuration.id());
    entity.setLastEdit(configuration.lastEdit());
    return entity;
  }

  /**
   * Maps a charge point entity to the corresponding domain model.
   *
   * @param entity the entity to map.
   * @return the corresponding domain model.
   */
  static ChargePoint toChargePoint(ChargePointEntity entity) {
    var configuration =
        entity.getConfiguration() == null ? null : toConfiguration(entity.getConfiguration());
    return new ChargePoint(
        entity.getId(),
        entity.getSerialNumberChargepoint(),
        entity.getType(),
        entity.getConstructor(),
        entity.getClientId(),
        entity.getLastUpdate(),
        entity.getError(),
        entity.isState(),
        entity.getStep(),
        entity.getStatus(),
        configuration);
  }

  /**
   * Maps a charge point domain model to the corresponding entity.
   *
   * @param chargePoint the domain model to map.
   * @param configurationRepository the repository used to resolve the configuration.
   * @return the corresponding entity.
   */
  static ChargePointEntity toChargePointEntity(
      ChargePoint chargePoint, ConfigurationJpaRepository configurationRepository) {
    ConfigurationEntity configuration = null;
    if (chargePoint.configuration() != null) {
      configuration =
          configurationRepository
              .findById(chargePoint.configuration().id())
              .orElseGet(() -> ConfigurationEntity.reference(chargePoint.configuration().id()));
    }
    var entity =
        new ChargePointEntity(
            chargePoint.serialNumber(),
            chargePoint.type(),
            chargePoint.constructor(),
            chargePoint.clientId(),
            configuration);
    entity.setId(chargePoint.id());
    entity.setLastUpdate(chargePoint.lastUpdate());
    entity.setError(chargePoint.error());
    entity.setState(chargePoint.state());
    entity.setStep(chargePoint.step());
    entity.setStatus(chargePoint.statusProcess());
    return entity;
  }

  /**
   * Maps a set of allowed type entities to the corresponding domain models.
   *
   * @param entities the entities to map.
   * @return the corresponding domain models.
   */
  static Set<TypeAllowed> toTypesAllowed(Set<TypeAllowedEntity> entities) {
    return entities.stream().map(EntityMapper::toTypeAllowed).collect(Collectors.toSet());
  }
}
