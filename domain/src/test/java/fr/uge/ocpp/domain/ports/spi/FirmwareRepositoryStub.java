package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Firmware;
import fr.uge.ocpp.domain.models.Firmwares;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import fr.uge.ocpp.domain.models.TypeAllowed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class FirmwareRepositoryStub implements FirmwareRepository {

  private final List<Firmware> firmwares = new ArrayList<>();

  public void add(Firmware firmware) {
    firmwares.add(firmware);
  }

  public void clear() {
    firmwares.clear();
  }

  @Override
  public Optional<Firmware> findByVersion(String version) {
    return firmwares.stream().filter(firmware -> firmware.version().equals(version)).findFirst();
  }

  @Override
  public Firmwares findAllByTypeAllowedAsc(TypeAllowed typeAllowed) {
    return firmwares.stream()
      .filter(firmware -> firmware.typesAllowed().typesAllowedSet().contains(typeAllowed))
      .collect(collectingAndThen(toList(), Firmwares::new));
  }

  @Override
  public Firmwares findAllByTypeAllowedDesc(TypeAllowed typeAllowed) {
    return firmwares.stream()
      .filter(firmware -> firmware.typesAllowed().typesAllowedSet().contains(typeAllowed))
      .sorted(Comparator.comparing(Firmware::version).reversed())
      .collect(collectingAndThen(toList(), Firmwares::new));
  }

  @Override
  public Firmwares findAllByOrderByIdDesc() {
    return firmwares.stream()
      .sorted(Comparator.comparingLong(Firmware::id).reversed())
      .collect(collectingAndThen(toList(), Firmwares::new));
  }

  @Override
  public PageResult<Firmware> findAllByOrderByIdDesc(PageQuery pageQuery) {
    List<Firmware> result = firmwares.stream()
      .skip((long) pageQuery.page() * pageQuery.size())
      .sorted(Comparator.comparingLong(Firmware::id).reversed())
      .limit(pageQuery.size())
      .toList();

    return new PageResult<>(result, pageQuery.page(), pageQuery.size(), firmwares.size());
  }

  @Override
  public Optional<Firmware> findByUrl(String url) {
    return firmwares.stream().filter(firmware -> firmware.url().equals(url)).findFirst();
  }

  @Override
  public Optional<Firmware> findById(long id) {
    return firmwares.stream().filter(firmware -> firmware.id().equals(id)).findFirst();
  }
}