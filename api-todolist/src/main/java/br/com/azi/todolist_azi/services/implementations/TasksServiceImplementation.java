package br.com.azi.todolist_azi.services.implementations;

import br.com.azi.todolist_azi.domain.tasks.Tasks;
import br.com.azi.todolist_azi.dtos.CreateTaskDTO;
import br.com.azi.todolist_azi.dtos.UpdateTaskDTO;
import br.com.azi.todolist_azi.exceptions.EmptyTitleTaskException;
import br.com.azi.todolist_azi.exceptions.TaskNotFoundException;
import br.com.azi.todolist_azi.repositories.ITasksRepository;
import br.com.azi.todolist_azi.services.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksServiceImplementation implements TasksService {
    @Autowired
    private ITasksRepository tasksRepository;

    @Override
    public Tasks createTask(CreateTaskDTO data) {
        if(data.title().isEmpty()) {
            throw new EmptyTitleTaskException();
        }

        CreateTaskDTO task = new CreateTaskDTO(data.title(), data.description());

        Tasks newtask = new Tasks(task);

        return this.tasksRepository.save(newtask);
    }

    @Override
    public List<Tasks> getAllTasks() {
        return this.tasksRepository.findAll();
    }

    @Override
    public Tasks getTaskById(Long taskId) {
        Tasks taskFounded = this.tasksRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());

        return taskFounded;
    }

    @Override
    public Tasks updateTask(Long taskId, UpdateTaskDTO updateTaskDTO) {
        Tasks retrievedTask = this.tasksRepository.findById(taskId).orElseThrow(()-> new TaskNotFoundException());

        updateTaskDTO.title().ifPresent(retrievedTask::setTitle);
        updateTaskDTO.description().ifPresent(retrievedTask::setDescription);
        updateTaskDTO.completed().ifPresent(retrievedTask::setCompleted);

        return this.tasksRepository.save(retrievedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        Tasks taskToDelete = this.tasksRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());
        this.tasksRepository.deleteById(taskId);
    }
}
