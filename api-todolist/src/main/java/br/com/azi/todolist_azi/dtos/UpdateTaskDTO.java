package br.com.azi.todolist_azi.dtos;

import java.util.Optional;

public record UpdateTaskDTO(Optional<String> title, Optional<String> description, Optional<Boolean> completed) {
    public UpdateTaskDTO {
        title = title == null ? Optional.empty() : title;
        description = description == null ? Optional.empty() : description;
        completed = completed == null ? Optional.empty() : completed;
    }
}
