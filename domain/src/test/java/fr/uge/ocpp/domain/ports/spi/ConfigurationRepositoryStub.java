package fr.uge.ocpp.domain.ports.spi;

import fr.uge.ocpp.domain.models.Configuration;
import fr.uge.ocpp.domain.models.Configurations;
import fr.uge.ocpp.domain.models.PageQuery;
import fr.uge.ocpp.domain.models.PageResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
    List<Configuration> configurationList = configurations.stream()
      .skip((long) pageQuery.page() * pageQuery.size())
      .sorted(Comparator.comparingLong(Configuration::id).reversed())
      .limit(pageQuery.size())
      .toList();

    return new PageResult<>(configurationList, pageQuery.page(), pageQuery.size(), configurations.size());
  }

  @Override
  public Configuration save(Configuration configuration) {
    configurations.add(configuration);

    return configuration;
  }

  @Override
  public Optional<Configuration> findById(long id) {
    return configurations.stream().filter(configuration -> configuration.id().equals(id)).findFirst();
  }
}