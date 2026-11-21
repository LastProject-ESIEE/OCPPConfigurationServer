package fr.uge.ocpp.domain.ports.spi;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import fr.uge.ocpp.domain.models.ChargePoint;
import fr.uge.ocpp.domain.models.ChargePoints;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ChargePointRepositoryStub implements ChargePointRepository {

  private final List<ChargePoint> chargePoints = new ArrayList<>();

  public void clear() {
    chargePoints.clear();
  }

  public List<ChargePoint> chargePoints() {
    return chargePoints;
  }

  public void add(ChargePoint chargePoint) {
    chargePoints.add(chargePoint);
  }

  @Override
  public Optional<ChargePoint> findBySerialNumberChargepointAndConstructor(
      String serialNumber, String constructor) {
    return chargePoints.stream()
        .filter(
            chargePoint ->
                chargePoint.serialNumber().equals(serialNumber)
                    && chargePoint.constructor().equals(constructor))
        .findFirst();
  }

  @Override
  public ChargePoints findAllByOrderByIdDesc() {
    return chargePoints.stream()
        .sorted(Comparator.comparingLong(ChargePoint::id).reversed())
        .collect(collectingAndThen(toList(), ChargePoints::new));
  }

  @Override
  public PageResult<ChargePoint> findAllByOrderByIdDesc(PageQuery pageQuery) {
    List<ChargePoint> result =
        chargePoints.stream()
            .sorted(Comparator.comparingLong(ChargePoint::id).reversed())
            .skip((long) pageQuery.page() * pageQuery.size())
            .limit(pageQuery.size())
            .toList();

    return new PageResult<>(result, pageQuery.page(), pageQuery.size(), chargePoints.size());
  }

  @Override
  public PageResult<ChargePoint> search(String request, PageQuery pageQuery) {
    List<ChargePoint> matching = matching(request).toList();
    List<ChargePoint> result =
        matching.stream()
            .sorted(Comparator.comparingLong(ChargePoint::id).reversed())
            .skip((long) pageQuery.page() * pageQuery.size())
            .limit(pageQuery.size())
            .toList();

    return new PageResult<>(result, pageQuery.page(), pageQuery.size(), matching.size());
  }

  @Override
  public long count(String request) {
    return matching(request).count();
  }

  private Stream<ChargePoint> matching(String request) {
    if (request == null || request.isBlank()) {
      return chargePoints.stream();
    }
    var pattern = request.toLowerCase();
    return chargePoints.stream()
        .filter(
            chargePoint ->
                chargePoint.serialNumber().toLowerCase().contains(pattern)
                    || chargePoint.type().toLowerCase().contains(pattern)
                    || chargePoint.constructor().toLowerCase().contains(pattern)
                    || chargePoint.clientId().toLowerCase().contains(pattern));
  }

  @Override
  public Optional<ChargePoint> findById(long id) {
    return chargePoints.stream()
        .filter(chargePoint -> chargePoint.id() != null && chargePoint.id() == id)
        .findFirst();
  }

  @Override
  public ChargePoint save(ChargePoint chargePoint) {
    chargePoints.removeIf(
        existing -> chargePoint.id() != null && chargePoint.id().equals(existing.id()));
    chargePoints.add(chargePoint);

    return chargePoint;
  }
}
