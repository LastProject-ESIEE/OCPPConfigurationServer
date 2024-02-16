package fr.uge.chargepointconfiguration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A controller that allows to forward the requests that does not match any endpoint to the
 * frontend router.
 */
@Controller
public class SpaController {

  /**
   * A method that forwards the requests to the frontend root.
   *
   * @return a redirect to the view of the frontend root
   */
  @RequestMapping(value = "/{path:[^\\.]*}")
  public String forward() {
    return "forward:/";
  }
}
