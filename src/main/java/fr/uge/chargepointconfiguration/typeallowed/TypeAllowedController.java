package fr.uge.chargepointconfiguration.typeallowed;

import fr.uge.chargepointconfiguration.chargepoint.Chargepoint;
import fr.uge.chargepointconfiguration.chargepoint.ChargepointDto;
import fr.uge.chargepointconfiguration.firmware.FirmwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the firmware entity.
 */
@RequestMapping("/api/type")
@RestController
@Tag(name = "TypeAllowed", description = "The type allowed API")
public class TypeAllowedController {

  private final TypeAllowedService typeAllowedService;


  /**
   * FirmwareController's constructor.
   *
   * @param typeAllowedService A FirmwareRepository.
   */
  @Autowired
  public TypeAllowedController(TypeAllowedService typeAllowedService) {
    this.typeAllowedService = typeAllowedService;
  }

  /**
   * Returns a list of all type allowed.
   *
   * @return A list of all type allowed.
   */
  @Operation(summary = "Get all the type allowed")
  @ApiResponse(responseCode = "200",
          description = "Found all type allowed",
          content = { @Content(mediaType = "application/json",
                  schema = @Schema(implementation = TypeAllowedDto.class))
          })
  @GetMapping(value = "/all")
  public List<TypeAllowedDto> getAllTypeAllowed() {
    return typeAllowedService.getAll();
  }

}
