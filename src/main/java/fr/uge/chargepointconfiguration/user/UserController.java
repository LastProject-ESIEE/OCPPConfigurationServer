package fr.uge.chargepointconfiguration.user;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for user management.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
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
    return userService.getUserById(id);
  }

  /**
   * retrieve info in database for all users.
   *
   * @return Details about all the users.
   */
  @GetMapping("/all")
  public List<UserDto> getAllUsers() {
    return userService.getAllUsers();
  }

  /**
   * retrieve info in database for the current user.
   *
   * @return Details about the current user.
   */
  @GetMapping("/me")
  public UserDto getAuthenticatedUser() {
    return userService.getAuthenticatedUser();
  }

  /**
   * Update the password of the user.
   *
   * @param changePasswordUserDto a ChangePassworddUserDto.
   * @return a ChangePasswordUserDto.
   */
  @PostMapping("/updatePassword")
  public ResponseEntity<User> postNewPasswordUser(
          @Parameter(
                  name = "String",
                  description = "Old and new password",
                  example = """
                          {
                            "oldPassword": "String"
                            "newPassword": "String"
                          }""",
                  required = true)
          @RequestBody ChangePasswordUserDto changePasswordUserDto) {
    var user = userService.updatePassword(changePasswordUserDto);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
