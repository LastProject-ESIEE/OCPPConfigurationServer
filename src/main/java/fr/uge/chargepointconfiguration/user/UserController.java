package fr.uge.chargepointconfiguration.user;

import fr.uge.chargepointconfiguration.shared.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for user management.
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "The user API")
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
  @Operation(summary = "Get a user by its id.")
  @ApiResponse(responseCode = "200",
        description = "Found the corresponding users",
        content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class)
        )
  )
  @GetMapping("/{id}")
  public UserDto getUserById(
        @Parameter(description = "The user you are looking for.")
        @PathVariable int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    System.out.println("getUser " + id);
    return userService.getUserById(id).toDto();
  }

  /**
   * retrieve info in database for all users.
   *
   * @return Details about all the users.
   */
  @Operation(summary = "Get all users.")
  @ApiResponse(responseCode = "200",
        description = "Found all users",
        content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class)
        )
  )
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
  @Operation(summary = "Get the current authenticated user.")
  @ApiResponse(responseCode = "200",
        description = "Found the authenticated user",
        content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class)
        )
  )
  @GetMapping("/me")
  public UserDto getAuthenticatedUser() {
    return userService.getAuthenticatedUser().toDto();
  }

  /**
   * Update the password of the user.
   *
   * @param changePasswordUserDto a ChangePasswordUserDto.
   * @return a ResponseEntity of UserDto.
   */
  @Operation(summary = "Update password")
  @ApiResponse(responseCode = "200",
        description = "Update the password of the current user",
        content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class)
        )
  )
  @PostMapping("/updatePassword")
  public ResponseEntity<UserDto> updatePassword(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Old and new password.",
              required = true
        )
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
        content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class)
        )
  )
  @PostMapping("/updateRole")
  public ResponseEntity<UserDto> updateRole(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "JSON with id and new role of the user.",
              required = true
        )
        @RequestBody ChangeRoleUserDto changeRoleUserDto) {
    var user = userService.updateRole(changeRoleUserDto).toDto();
    return new ResponseEntity<>(user, HttpStatus.OK);
  }


  /**
   * Search for {@link UserDto} with a pagination.
   *
   * @param size Desired size of the requested page.
   * @param page Requested page.
   * @param sortBy The column you want to sort by. Must be an attribute of
   *               the {@link UserDto}.
   * @param order The order of the sort. Must be "asc" or "desc".
   * @return A page containing a list of {@link UserDto}
   */
  @Operation(summary = "Search for users")
  @ApiResponse(responseCode = "200",
        description = "Found users",
        content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = UserDto.class))
        })
  @GetMapping(value = "/search")
  public PageDto<UserDto> getPage(
        @Parameter(description = "Desired size of the requested page.")
        @RequestParam(required = false, defaultValue = "10") int size,

        @Parameter(description = "Requested page.")
        @RequestParam(required = false, defaultValue = "0") int page,

        @Parameter(description =
              "The column you want to sort by. Must be an attribute of the users.")
        @RequestParam(required = false, defaultValue = "id") String sortBy,

        @Parameter(description = "The order of the sort. must be \"asc\" or \"desc\"")
        @RequestParam(required = false, defaultValue = "asc") String order
  ) {
    var total = userService.countTotal();
    var data = userService.getPage(
                PageRequest.of(page, size, Sort.by(Sort.Order.by(order).getDirection(), sortBy))
          ).stream()
          .map(User::toDto)
          .toList();
    return new PageDto<>(total, page, size, data);
  }
}
