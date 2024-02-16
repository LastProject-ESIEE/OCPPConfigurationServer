package fr.uge.chargepointconfiguration;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the standard index.
 */
@RestController
@RequestMapping(value = "/api")
public class HomeController {


  /**
   * dzefzefzeff.
   *
   * @return zrz
   */
  @RequestMapping
  public String api() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Print user details
    System.out.println("Authenticated user: " + authentication.getName());
    System.out.println("User authorities: " + authentication.getAuthorities());
    System.out.println("User details: " + authentication.getDetails());

    return "api";
  }

  /**
   * dzefzefzeff.
   *
   * @return zrz
   */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public String admin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Print user details
    System.out.println("Authenticated user: " + authentication.getName());
    System.out.println("User authorities: " + authentication.getAuthorities());
    System.out.println("User details: " + authentication.getDetails());

    return "admin api";
  }

  /**
   * dzefzefzeff.
   *
   * @return zrz
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user")
  public String user() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Print user details
    System.out.println("Authenticated user: " + authentication.getName());
    System.out.println("User authorities: " + authentication.getAuthorities());
    System.out.println("User details: " + authentication.getDetails());

    return "user api";
  }
}