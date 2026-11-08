package fr.uge.ocpp.database.dao;

import fr.uge.ocpp.database.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data access to the application user table. */
@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {

  /**
   * Finds the user with the given email.
   *
   * @param email the email of the user.
   * @return the corresponding user if any.
   */
  UserEntity findByEmail(String email);
}
