package fr.uge.chargepointconfiguration.user;

import fr.uge.chargepointconfiguration.entities.User;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for user management.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * retrieve info in database for a given user.
   *
   * @param id The id of the user.
   * @return Details about the user.
   */
  @GetMapping("/{id}")
  public UserDto getUserById(@PathVariable int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    System.out.println("getUser " + id);
    return userRepository.findById(id).toDto();
  }

  /**
   * retrieve info in database for all users.
   *
   * @return Details about all the users.
   */
  @GetMapping("/all")
  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream().map(User::toDto).toList();
  }

  /**
   * retrieve info in database for the current user.
   *
   * @return Details about the current user.
   */
  @GetMapping("/me")
  public UserDto getAuthenticatedUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var email = authentication.getName();
    return userRepository.findByEmail(email).toDto();
  }
}
