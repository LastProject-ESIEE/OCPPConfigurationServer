package fr.uge.chargepointconfiguration.errors;

import fr.uge.chargepointconfiguration.errors.exceptions.BadRequestException;
import fr.uge.chargepointconfiguration.errors.exceptions.EntityAlreadyExistingException;
import fr.uge.chargepointconfiguration.errors.exceptions.EntityNotFoundException;
import fr.uge.chargepointconfiguration.errors.exceptions.ForbiddenOperationException;
import fr.uge.chargepointconfiguration.errors.exceptions.IllegalOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller to handle errors with exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(EntityAlreadyExistingException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorMessage> handleEntityAlreadyExistingException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorMessage> handleBadRequestException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalOperationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorMessage> handleIllegalOperationException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ ForbiddenOperationException.class, AccessDeniedException.class })
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ErrorMessage> handleForbiddenOperationException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.FORBIDDEN);
  }
}