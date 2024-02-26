package fr.uge.chargepointconfiguration.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    var userDto = getAuthenticatedUser();
    var id = userDto.id();
    var user = userRepository.findById(id);
    if (user == null) {
      throw new IllegalArgumentException("User not found with this id : " + id);
    }
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
  public UserDto getUserById(int id) {
    return userRepository.findById(id).toDto();
  }

  /**
   * retrieve info in database for all users.
   *
   * @return Details about all the users.
   */
  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream().map(User::toDto).toList();
  }

  /**
   * retrieve info in database for the current user.
   *
   * @return Details about the current user.
   */
  public UserDto getAuthenticatedUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var email = authentication.getName();
    return userRepository.findByEmail(email).toDto();
  }
}
