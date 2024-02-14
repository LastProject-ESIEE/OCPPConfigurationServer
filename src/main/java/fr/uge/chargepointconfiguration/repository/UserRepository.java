package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.User;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the user.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  List<User> findAll();
}