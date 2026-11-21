package fr.uge.ocpp.database.specifications;

import fr.uge.ocpp.database.entities.ChargePointEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

/** Specifications for the charge point table. */
@UtilityClass
public class ChargePointSpecifications {

  /**
   * Matches the charge points whose serial number, type, constructor or client id contains the
   * given request, ignoring case. A blank request matches everything.
   *
   * @param request the search request.
   * @return the corresponding specification.
   */
  public Specification<ChargePointEntity> request(String request) {
    if (request == null || request.isBlank()) {
      return (root, query, cb) -> cb.conjunction();
    }
    var pattern = "%" + request.toLowerCase() + "%";
    return (root, query, cb) ->
        cb.or(
            cb.like(cb.lower(root.get("serialNumberChargepoint")), pattern),
            cb.like(cb.lower(root.get("constructor")), pattern),
            cb.like(cb.lower(root.get("type")), pattern),
            cb.like(cb.lower(root.get("clientId")), pattern));
  }
}
