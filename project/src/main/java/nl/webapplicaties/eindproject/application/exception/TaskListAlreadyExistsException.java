package nl.webapplicaties.eindproject.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Task list already exists!")
public class TaskListAlreadyExistsException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Task list already exists";
    }
}
