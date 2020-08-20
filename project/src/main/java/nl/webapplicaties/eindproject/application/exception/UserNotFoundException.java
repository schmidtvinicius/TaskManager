package nl.webapplicaties.eindproject.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found!")
public class UserNotFoundException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User not found!";
    }
}
