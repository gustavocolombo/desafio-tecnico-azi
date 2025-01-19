package br.com.azi.todolist_azi.infra;

import br.com.azi.todolist_azi.dtos.ExceptionDTO;
import br.com.azi.todolist_azi.exceptions.EmptyTitleTaskException;
import br.com.azi.todolist_azi.exceptions.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    private ResponseEntity<ExceptionDTO> treatNotFoundTask(TaskNotFoundException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDTO);
    }

    @ExceptionHandler(EmptyTitleTaskException.class)
    private ResponseEntity<ExceptionDTO> treatEmptyTitleTaskField(EmptyTitleTaskException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
    }
}
