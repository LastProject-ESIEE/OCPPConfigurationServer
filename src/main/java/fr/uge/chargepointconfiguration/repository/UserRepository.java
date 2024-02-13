package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {}