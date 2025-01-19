package br.com.azi.todolist_azi.exceptions;

public class EmptyTitleTaskException extends RuntimeException {
    public EmptyTitleTaskException() {
        super("The task title field cannot be empty");
    }

    public EmptyTitleTaskException(String message) {
        super(message);
    }
}
