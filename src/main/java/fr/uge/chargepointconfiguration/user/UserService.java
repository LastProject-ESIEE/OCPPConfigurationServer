package fr.uge.chargepointconfiguration.user;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A UserService doing database manipulations.
 */
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Update the password of the user id.
   *
   * @param changePasswordUserDto of the user.
   * @return a User.
   */
  public User updatePassword(ChangePasswordUserDto changePasswordUserDto) {
    var user = getAuthenticatedUser();
    if (!passwordEncoder.matches(changePasswordUserDto.oldPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Bad password");
    }
    var encodedPassword = passwordEncoder.encode(changePasswordUserDto.newPassword());
    user.setPassword(encodedPassword);
    return userRepository.save(user);
  }

  /**
   * Retrieve information about a user.
   *
   * @param id of the user.
   * @return information about the user.
   */
  public User getUserById(int id) {
    return userRepository.findById(id);
  }

  /**
   * Retrieve info in database for all users.
   *
   * @return Details about all the users.
   */
  public List<User> getAllUsers() {
    return userRepository.findAll().stream().toList();
  }

  /**
   * Retrieve info in database for the current user.
   *
   * @return Details about the current user.
   */
  public User getAuthenticatedUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var email = authentication.getName();
    return userRepository.findByEmail(email);
  }

  /**
   * Update the role of the user.
   *
   * @param changeRoleUserDto a ChangeRoleUserDto.
   * @return a User updated.
   */
  public User updateRole(ChangeRoleUserDto changeRoleUserDto) {
    var user = getUserById(changeRoleUserDto.id());
    var role = changeRoleUserDto.role();
    var test = Arrays.asList(User.Role.values()).contains(role);
    if (!test) {
      throw new IllegalArgumentException("This role doesn't exist. Verify your parameter.");
    }
    user.setRole(role);
    return userRepository.save(user);
  }

  public long countTotal() {
    return userRepository.count();
  }

  /**
   * Search for {@link UserDto} with a pagination.
   *
   * @param pageable The page requested
   * @return the list of corresponding {@link UserDto}
   */
  public List<UserDto> getPage(PageRequest pageable) {
    return userRepository.findAll(pageable)
          .stream()
          .map(User::toDto)
          .toList();
  }
}
