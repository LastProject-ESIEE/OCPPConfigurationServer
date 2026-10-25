package fr.uge.ocpp.domain.models;

import java.time.LocalDateTime;

public record Configuration(Long id,
                            String name,
                            String description,
                            LocalDateTime lastEdit,
                            String configuration,
                            Firmware firmware) {

  public Configuration withId(Long id) {
    return new Configuration(id, name, description, lastEdit, configuration, firmware);
  }

  public Configuration withName(String name) {
    return new Configuration(id, name, description, lastEdit, configuration, firmware);
  }

  public Configuration withDescription(String description) {
    return  new Configuration(id, name, description, lastEdit, configuration, firmware);
  }

  public  Configuration withLastEdit(LocalDateTime lastEdit) {
    return new Configuration(id, name, description, lastEdit, configuration, firmware);
  }

  public Configuration withConfiguration(String configuration) {
    return new Configuration(id, name, description, lastEdit, configuration, firmware);
  }

  public Configuration withFirmware(Firmware firmware) {
    return new Configuration(id, name, description, lastEdit, configuration, firmware);
  }
}
