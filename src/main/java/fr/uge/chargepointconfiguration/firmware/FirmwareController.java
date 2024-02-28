package fr.uge.chargepointconfiguration.firmware;

import fr.uge.chargepointconfiguration.shared.PageDto;
import fr.uge.chargepointconfiguration.typeallowed.TypeAllowed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the firmware entity.
 */
@RequestMapping("/api/firmware")
@RestController
@Tag(name = "Firmware", description = "The firmware API")
public class FirmwareController {

  private final FirmwareService firmwareService;

  /**
   * FirmwareController's constructor.
   *
   * @param firmwareService A FirmwareRepository.
   */
  @Autowired
  public FirmwareController(FirmwareService firmwareService) {
    this.firmwareService = firmwareService;
  }

  /**
   * Returns all the firmwares.
   *
   * @return A list of all the firmwares.
   */
  @Operation(summary = "Get all the firmwares")
  @ApiResponse(responseCode = "200",
      description = "Found all the firmwares",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = Firmware.class)
      )
  )
  @GetMapping(value = "/all")
  public List<FirmwareDto> getAllFirmwares() {
    return firmwareService.getAllFirmwares().stream().map(Firmware::toDto).toList();
  }

  /**
   * Returns a firmware according to the given id.<br>
   * The optional will be empty if a firmware could not be found.
   *
   * @param id An int.
   * @return An optional of firmware.
   */
  @Operation(summary = "Get a firmware by its id")
  @ApiResponses(value = {
      @ApiResponse(
            responseCode = "200",
            description = "Found the firmware",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Firmware.class)
            )
      ),
      @ApiResponse(
            responseCode = "404",
            description = "This firmware does not exist"
      )
  })
  @GetMapping(value = "/{id}")
  public Optional<FirmwareDto> getFirmwareById(
      @Parameter(description = "id of firmware to be searched") @PathVariable int id) {
    // TODO : exception BAD REQUEST si id est pas un nombre
    return Optional.of(firmwareService.getFirmwareById(id).orElseThrow().toDto());
  }


  /**
   * Search for {@link FirmwareDto} with a pagination.
   *
   * @param size Desired size of the requested page.
   * @param page Requested page.
   * @param sortBy The column you want to sort by. Must be an attribute of
   *               the {@link FirmwareDto}.
   * @param order The order of the sort. Must be "asc" or "desc".
   * @return A page containing a list of {@link FirmwareDto}
   */
  @Operation(summary = "Search for configurations")
  @ApiResponse(responseCode = "200",
        description = "Found configurations",
        content = { @Content(mediaType = "application/json",
              schema = @Schema(implementation = FirmwareDto.class))
        })
  @GetMapping(value = "/search")
  public PageDto<FirmwareDto> getPage(
        @Parameter(description = "Desired size of the requested page.")
        @RequestParam(required = false, defaultValue = "10") int size,

        @Parameter(description = "Requested page.")
        @RequestParam(required = false, defaultValue = "0") int page,

        @Parameter(description =
              "The column you want to sort by. Must be an attribute of the configuration.")
        @RequestParam(required = false, defaultValue = "id") String sortBy,

        @Parameter(description = "The order of the sort. must be \"asc\" or \"desc\"")
        @RequestParam(required = false, defaultValue = "asc") String order
  ) {
    var total = firmwareService.countTotal();

    var data = firmwareService.getPage(
                PageRequest.of(page, size, Sort.by(Sort.Order.by(order).getDirection(), sortBy))
          ).stream()
          .map(entity -> new FirmwareDto(entity.getId(),
                entity.getUrl(),
                entity.getVersion(),
                entity.getConstructor(),
                entity.getTypesAllowed().stream().map(TypeAllowed::toDto)
                      .collect(Collectors.toSet())
          ))
          .toList();

    return new PageDto<>(total, page, size, data);
  }
}
