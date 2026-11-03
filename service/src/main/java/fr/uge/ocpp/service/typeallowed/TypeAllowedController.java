package fr.uge.ocpp.service.typeallowed;

import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.ports.api.TypeAllowedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The REST endpoints of the allowed type API. */
@RestController
@RequestMapping("/api/type")
@Tag(name = "Allowed type", description = "The allowed type API")
public class TypeAllowedController {

  private final TypeAllowedService typeAllowedService;

  /**
   * Creates the controller.
   *
   * @param typeAllowedService the allowed type use cases.
   */
  public TypeAllowedController(TypeAllowedService typeAllowedService) {
    this.typeAllowedService = typeAllowedService;
  }

  /**
   * Returns all the allowed types.
   *
   * @return all the allowed types.
   */
  @Operation(summary = "Get all the allowed types")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found all the allowed types",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TypeAllowedDto.class)))),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role")
      })
  @GetMapping(value = "/all")
  @PreAuthorize("hasRole('EDITOR')")
  public List<TypeAllowedDto> getAllTypeAllowed() {
    return typeAllowedService.findAll().typesAllowedSet().stream().map(TypeAllowedDto::of).toList();
  }

  /**
   * Creates an allowed type.
   *
   * @param createTypeAllowedDto the values of the allowed type.
   * @return the created allowed type.
   */
  @Operation(summary = "Create an allowed type")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "The allowed type was created",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TypeAllowedDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid allowed type values"),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication required",
            content = @Content),
        @ApiResponse(responseCode = "403", description = "Requires the EDITOR role"),
        @ApiResponse(responseCode = "409", description = "This allowed type already exists")
      })
  @PostMapping("/create")
  @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<TypeAllowedDto> create(
      @RequestBody CreateTypeAllowedDto createTypeAllowedDto) {
    TypeAllowed typeAllowed =
        typeAllowedService.save(
            new TypeAllowed(null, createTypeAllowedDto.constructor(), createTypeAllowedDto.type()));
    return new ResponseEntity<>(TypeAllowedDto.of(typeAllowed), HttpStatus.CREATED);
  }
}
