package fr.esgi.projectservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NameMustBeNotNull extends RuntimeException{

    public NameMustBeNotNull(String message) {
        super(message);
    }
}
