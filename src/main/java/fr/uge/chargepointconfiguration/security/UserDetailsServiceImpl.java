package fr.uge.chargepointconfiguration.security;

import fr.uge.chargepointconfiguration.user.UserRepository;
import java.util.Objects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Loads the information of the connected user.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    Objects.requireNonNull(userRepository);
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException("User does not exist");
    }
    return new UserDetailsImpl(user);
  }
}
