package nl.webapplicaties.eindproject.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User already exists!")
public class UserAlreadyExistsException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User already exists";
    }
}
