package nl.webapplicaties.eindproject.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Category already exist!")
public class CategoryAlreadyExistException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Category already exists!";
    }
}
