package br.com.azi.todolist_azi.services;

import br.com.azi.todolist_azi.domain.tasks.Tasks;
import br.com.azi.todolist_azi.dtos.CreateTaskDTO;
import br.com.azi.todolist_azi.dtos.UpdateTaskDTO;

import java.util.List;

public interface TasksService {
    Tasks createTask(CreateTaskDTO data) throws Exception;

    List<Tasks> getAllTasks();

    Tasks getTaskById(Long taskId) throws Exception;

    Tasks updateTask(Long id, UpdateTaskDTO updateTaskDTO) throws Exception;

    void deleteTask(Long taskId) throws Exception;
}
