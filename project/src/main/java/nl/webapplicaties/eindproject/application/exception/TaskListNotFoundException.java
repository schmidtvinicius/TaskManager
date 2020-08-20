package nl.webapplicaties.eindproject.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Task list not found!")
public class TaskListNotFoundException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Task list not found!";
    }
}
