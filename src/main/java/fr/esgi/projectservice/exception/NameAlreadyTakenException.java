package fr.esgi.projectservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NameAlreadyTakenException extends RuntimeException{

    public NameAlreadyTakenException(String message) {
        super(message);
    }
}
