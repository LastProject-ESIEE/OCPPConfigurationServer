package fr.uge.ocpp.service.security;

import fr.uge.ocpp.database.dao.UserDao;
import java.util.Objects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** Loads the application users from the database for Spring Security. */
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserDao userDao;

  /**
   * Creates the service.
   *
   * @param userDao the Spring Data repository of users.
   */
  public UserDetailsServiceImpl(UserDao userDao) {
    this.userDao = Objects.requireNonNull(userDao);
  }

  /**
   * Loads the user with the given email.
   *
   * @param username the email of the user.
   * @return the principal of the user.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userDao.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("User does not exist");
    }
    return new UserDetailsImpl(user);
  }
}
