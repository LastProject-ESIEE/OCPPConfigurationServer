package fr.uge.ocpp.service.errors;

import fr.uge.ocpp.domain.exceptions.AlreadyExistingException;
import fr.uge.ocpp.domain.exceptions.BadRequestException;
import fr.uge.ocpp.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Translates the domain exceptions into HTTP responses. */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles the not found exceptions.
   *
   * @param ex the exception to handle.
   * @return a 404 response with the error message.
   */
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorMessage> handleNotFoundException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  /**
   * Handles the already existing exceptions.
   *
   * @param ex the exception to handle.
   * @return a 409 response with the error message.
   */
  @ExceptionHandler(AlreadyExistingException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorMessage> handleAlreadyExistingException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.CONFLICT);
  }

  /**
   * Handles the bad request exceptions.
   *
   * @param ex the exception to handle.
   * @return a 400 response with the error message.
   */
  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorMessage> handleBadRequestException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the invalid path argument exceptions.
   *
   * @param ex the exception to handle.
   * @return a 400 response with the error message.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorMessage> handleBadArguments(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage("Arguments invalides"), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles the access denied exceptions.
   *
   * @param ex the exception to handle.
   * @return a 403 response with the error message.
   */
  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ErrorMessage> handleForbiddenOperationException(RuntimeException ex) {
    return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), HttpStatus.FORBIDDEN);
  }
}
