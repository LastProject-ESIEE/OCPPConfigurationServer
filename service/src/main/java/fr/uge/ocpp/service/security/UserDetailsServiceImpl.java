package fr.uge.ocpp.service.security;

import fr.uge.ocpp.database.repositories.UserJpaRepository;
import java.util.Objects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/** Loads the application users from the database for Spring Security. */
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserJpaRepository userJpaRepository;

  /**
   * Creates the service.
   *
   * @param userJpaRepository the Spring Data repository of users.
   */
  public UserDetailsServiceImpl(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = Objects.requireNonNull(userJpaRepository);
  }

  /**
   * Loads the user with the given email.
   *
   * @param username the email of the user.
   * @return the principal of the user.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userJpaRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("User does not exist");
    }
    return new UserDetailsImpl(user);
  }
}
