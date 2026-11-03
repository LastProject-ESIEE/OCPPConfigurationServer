package fr.uge.ocpp.service.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The login endpoint issuing JWT access tokens. */
@RestController
@RequestMapping("/api/login")
@Tag(name = "Authentication", description = "The login API")
public class LoginController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  /**
   * Creates the controller.
   *
   * @param authenticationManager the authentication manager backed by the users table.
   * @param jwtService the JWT service.
   */
  public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  /**
   * Authenticates a user and returns a JWT.
   *
   * @param request the email and password of the user.
   * @return the signed JWT.
   */
  @Operation(summary = "Log in and get a JWT access token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Authenticated, JWT returned"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
      })
  @PostMapping
  public LoginResponse login(@RequestBody LoginRequest request) {
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    return new LoginResponse(jwtService.generateToken(authentication.getName()));
  }
}
