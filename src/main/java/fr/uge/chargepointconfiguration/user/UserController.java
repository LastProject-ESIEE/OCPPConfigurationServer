package fr.uge.chargepointconfiguration.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    return userService.getUserById(id).toDto();
  }

  /**
   * retrieve info in database for all users.
   *
   * @return Details about all the users.
   */
  @GetMapping("/all")
  public List<UserDto> getAllUsers() {
    return userService.getAllUsers()
            .stream()
            .map(User::toDto)
            .toList();
  }

  /**
   * retrieve info in database for the current user.
   *
   * @return Details about the current user.
   */
  @GetMapping("/me")
  public UserDto getAuthenticatedUser() {
    return userService.getAuthenticatedUser().toDto();
  }

  /**
   * Update the password of the user.
   *
   * @param changePasswordUserDto a ChangePassworddUserDto.
   * @return a ResponseEntity of UserDto.
   */
  @Operation(summary = "Update password")
  @ApiResponse(responseCode = "200",
      description = "Update the password of the current user",
      content = { @Content(mediaType = "application/json",
          schema = @Schema(implementation = User.class))
      })
  @PostMapping("/updatePassword")
  public ResponseEntity<UserDto> postUpdatePasswordUser(
          @Parameter(
                  name = "JSON with old and new password",
                  description = "Old and new password",
                  example = """
                          {
                            "oldPassword": "String",
                            "newPassword": "String"
                          }""",
                  required = true)
          @RequestBody ChangePasswordUserDto changePasswordUserDto) {
    var user = userService.updatePassword(changePasswordUserDto).toDto();
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Updadate the role of the user.
   *
   * @param changeRoleUserDto a ChangeRoleUserDto.
   * @return a ResponseEntity of UserDto.
   */
  @Operation(summary = "Update role")
  @ApiResponse(responseCode = "200",
          description = "Update the role of the current user",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = User.class))
          })
  @PostMapping("/updateRole")
  public ResponseEntity<UserDto> postUpdateRoleUser(
          @Parameter(
                  name = "JSON with id and new role of the user",
                  description = "Update the role of the user",
                  example = """
                          {
                            "id": "int",
                            "role": "String"
                          }
                          """,
                  required = true)
          @RequestBody ChangeRoleUserDto changeRoleUserDto) {
    var user = userService.updateRole(changeRoleUserDto).toDto();
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
