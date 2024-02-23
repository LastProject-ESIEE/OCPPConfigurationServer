package fr.uge.chargepointconfiguration.security;

import fr.uge.chargepointconfiguration.user.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of the spring security UserDetails.
 */
public class UserDetailsImpl implements UserDetails {

  private final User user;

  /**
   * UserDetailsImpl's constructor.
   *
   * @param user The connected user.
   */
  UserDetailsImpl(User user) {
    Objects.requireNonNull(user);
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    var authority = new SimpleGrantedAuthority(user.getRole().name());
    return List.of(authority);
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
