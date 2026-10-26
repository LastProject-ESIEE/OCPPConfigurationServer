package fr.uge.ocpp.database.repositories;

import fr.uge.ocpp.database.entities.ChargePointEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/** Spring Data access to the charge point table. */
@Repository
public interface ChargePointJpaRepository
    extends JpaRepository<ChargePointEntity, Long>, JpaSpecificationExecutor<ChargePointEntity> {

  /**
   * Finds the charge point with the given serial number and constructor.
   *
   * @param serialNumber the serial number of the charge point.
   * @param constructor  the constructor of the charge point.
   * @return the corresponding charge point if any.
   */
  Optional<ChargePointEntity> findBySerialNumberChargepointAndConstructor(String serialNumber,
                                                                          String constructor);

  /**
   * Finds all the charge points ordered by descending identifier.
   *
   * @return all the charge points.
   */
  List<ChargePointEntity> findAllByOrderByIdDesc();

  /**
   * Finds the charge points with a client id containing the given one, paginated.
   *
   * @param pageable the pagination information.
   * @param clientId the client id filter.
   * @return the corresponding page of charge points.
   */
  Page<ChargePointEntity> findAllByClientIdContainingIgnoreCaseOrderByIdDesc(
      Pageable pageable, String clientId);
}
