package fr.uge.chargepointconfiguration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the standard index.
 */
@Controller
public class HomeController {

  /**
   * Standard index controller.
   *
   * @return String.
   */
  @RequestMapping(value = "/")
  public String index() {
    return "index";
  }

}