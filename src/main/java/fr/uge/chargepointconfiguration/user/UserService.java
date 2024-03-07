package fr.uge.chargepointconfiguration.user;

import fr.uge.chargepointconfiguration.shared.SearchUtils;
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
   * @param id the id of the user to change.
   * @param role the new role to be applied.
   * @return a {@link User} updated.
   */
  public User updateRole(int id, User.Role role) {
    var user = userRepository.findById(id);
    var validRole = Arrays.asList(User.Role.values()).contains(role);
    if (!validRole) {
      throw new IllegalArgumentException("This role doesn't exist. Verify your parameter.");
    }
    if (user.getId() == getAuthenticatedUser().getId()) {
      throw new IllegalArgumentException("You can't change your role.");
    }
    user.setRole(role);
    return userRepository.save(user);
  }

  public long countTotal() {
    return userRepository.count();
  }

  /**
   * Search for {@link User} with a pagination.
   *
   * @param request the request used to search
   * @param pageable The page requested
   * @return the list of corresponding {@link User}
   */
  public List<User> search(String request, PageRequest pageable) {
    var condition = SearchUtils.computeSpecification(request, User.class);
    return userRepository.findAll(condition, pageable)
          .stream().toList();
  }

  /**
   * Create a new user in the database.
   *
   * @param createUserDto contains parameters of the new user.
   * @return the new User.
   * @throws AlreadyCreatedException when the user is already created.
   */
  public User createUser(
          CreateUserDto createUserDto
  ) throws AlreadyCreatedException {
    var password = passwordEncoder.encode(createUserDto.password());
    var user = new User(
            createUserDto.firstName(),
            createUserDto.lastName(),
            createUserDto.email(),
            password,
            createUserDto.role()
    );
    if (userRepository.findByEmail(user.getEmail()) != null) {
      throw new AlreadyCreatedException();
    }
    return userRepository.save(user);
  }
  
  /**
   * Deletes a user from its id.
   *
   * @param id the id of the user to be deleted
   */
  public void delete(int id) {
    if (getAuthenticatedUser().getId() == id) {
      throw new IllegalArgumentException("Cannot delete yourself");
    }
    userRepository.delete(userRepository.findById(id));
  }
}
