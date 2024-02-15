package fr.uge.chargepointconfiguration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the standard index.
 */
@RestController
@RequestMapping(value = "/api")
public class HomeController {

  @RequestMapping
  public String api() {
    return "api";
  }

}