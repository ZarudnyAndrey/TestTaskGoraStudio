package ru.zarudny.app.exception;

import java.lang.instrument.IllegalClassFormatException;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.zarudny.app.dto.error.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({EntityNotFoundException.class})
  public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
    log.error("Entity exception, bad request: {}", ex.getMessage());
    return ErrorResponse.builder()
        .message("Entity not exist.")
        .build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalClassFormatException.class)
  public ErrorResponse handleIllegalImageFormatException(IllegalClassFormatException ex) {
    log.error("File not supported: {}", ex.getMessage());
    return ErrorResponse.builder()
        .message("Illegal file format.")
        .build();
  }

  @ExceptionHandler(StorageException.class)
  public ResponseEntity<?> handleStorageException(StorageException ex) {
    log.error(ex.getMessage());
    return ResponseEntity.status(500).build();
  }
}
