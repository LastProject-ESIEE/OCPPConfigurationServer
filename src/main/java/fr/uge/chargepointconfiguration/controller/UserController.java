package fr.uge.chargepointconfiguration.controller;

import fr.uge.chargepointconfiguration.entities.User;
import fr.uge.chargepointconfiguration.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the firmware entity.
 */
@RestController
public class UserController {

  private final UserRepository userRepository;

  /**
   * FirmwareController's constructor.
   *
   * @param userRepository A UserRepository.
   */
  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Returns all users.
   *
   * @return A list of all users.
   */
  @GetMapping(value = "/users")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

}
