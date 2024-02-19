package fr.uge.chargepointconfiguration.repository;

import fr.uge.chargepointconfiguration.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for the user.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  /**
   * Returns a User from the database according to the email.
   *
   * @param email User's email.
   * @return The correct User or null if the user couldn't be found.
   */
  @Query("SELECT u FROM User u WHERE u.email = :email")
  User findByEmail(@Param("email") String email);

  User findById(int id);

  List<User> findAll();
}