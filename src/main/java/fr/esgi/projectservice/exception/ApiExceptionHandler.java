package fr.esgi.projectservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = NameAlreadyTakenException.class)
    public ResponseEntity<Object> handleNameAlreadyTakenException(NameAlreadyTakenException nameAlreadyTakenException){
        ApiException apiException = new ApiException(
                nameAlreadyTakenException.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now());

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NameMustBeNotNull.class)
    public ResponseEntity<Object> handleNameMustBeNotNullException(NameMustBeNotNull nameMustBeNotNull){
        ApiException apiException = new ApiException(
                nameMustBeNotNull.getMessage(),
                HttpStatus.NOT_ACCEPTABLE,
                ZonedDateTime.now());

        return new ResponseEntity<>(apiException, HttpStatus.NOT_ACCEPTABLE);
    }
}
