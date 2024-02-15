package fr.uge.chargepointconfiguration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the standard index.
 */
@RestController
public class HomeController {

  @RequestMapping(value = "/api")
  public String api() {
    return "api";
  }

}