package fr.uge.ocpp.service.security;

import fr.uge.ocpp.database.entities.UserEntity;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** The Spring Security principal of an application user. */
public class UserDetailsImpl implements UserDetails {

  private final UserEntity user;

  /**
   * Creates the principal.
   *
   * @param user the user entity of the principal.
   */
  UserDetailsImpl(UserEntity user) {
    this.user = Objects.requireNonNull(user);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
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
