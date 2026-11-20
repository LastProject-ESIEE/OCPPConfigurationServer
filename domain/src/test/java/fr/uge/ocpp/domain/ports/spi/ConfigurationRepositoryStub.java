package fr.uge.ocpp.domain.ports.spi;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ConfigurationRepositoryStub implements ConfigurationRepository {

  private final List<Configuration> configurations = new ArrayList<>();

  public void clear() {
    configurations.clear();
  }

  public List<Configuration> configurations() {
    return configurations;
  }

  public void add(Configuration configuration) {
    configurations.add(configuration);
  }

  @Override
  public Configurations findAllByOrderByIdDesc() {
    return configurations.stream()
        .filter(configuration -> configuration.id() != null)
        .sorted(Comparator.comparingLong(Configuration::id).reversed())
        .collect(collectingAndThen(toList(), Configurations::new));
  }

  @Override
  public PageResult<Configuration> findAllByOrderByIdDesc(PageQuery pageQuery) {
    List<Configuration> configurationList =
        configurations.stream()
            .skip((long) pageQuery.page() * pageQuery.size())
            .sorted(Comparator.comparingLong(Configuration::id).reversed())
            .limit(pageQuery.size())
            .toList();

    return new PageResult<>(
        configurationList, pageQuery.page(), pageQuery.size(), configurations.size());
  }

  @Override
  public PageResult<Configuration> search(String request, PageQuery pageQuery) {
    List<Configuration> matching = matching(request).toList();
    List<Configuration> result =
        matching.stream()
            .sorted(Comparator.comparingLong(Configuration::id).reversed())
            .skip((long) pageQuery.page() * pageQuery.size())
            .limit(pageQuery.size())
            .toList();

    return new PageResult<>(result, pageQuery.page(), pageQuery.size(), matching.size());
  }

  @Override
  public long count(String request) {
    return matching(request).count();
  }

  private Stream<Configuration> matching(String request) {
    if (request == null || request.isBlank()) {
      return configurations.stream();
    }
    var pattern = request.toLowerCase();
    return configurations.stream()
        .filter(
            configuration ->
                configuration.name().toLowerCase().contains(pattern)
                    || configuration.description().toLowerCase().contains(pattern));
  }

  @Override
  public Configuration save(Configuration configuration) {
    configurations.add(configuration);

    return configuration;
  }

  @Override
  public Optional<Configuration> findById(long id) {
    return configurations.stream()
        .filter(configuration -> configuration.id().equals(id))
        .findFirst();
  }
}
