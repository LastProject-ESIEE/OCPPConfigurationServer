package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.TypesAllowed;

public interface TypeAllowedRepository {

  TypesAllowed findAll();
}
