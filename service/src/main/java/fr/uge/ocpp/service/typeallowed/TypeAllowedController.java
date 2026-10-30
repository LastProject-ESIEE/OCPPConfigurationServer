package fr.uge.ocpp.service.typeallowed;

import fr.uge.ocpp.domain.models.TypeAllowed;
import fr.uge.ocpp.domain.ports.api.TypeAllowedService;
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
