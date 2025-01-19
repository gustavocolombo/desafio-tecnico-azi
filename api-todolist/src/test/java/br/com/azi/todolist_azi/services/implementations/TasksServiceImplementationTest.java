package br.com.azi.todolist_azi.services.implementations;

import br.com.azi.todolist_azi.domain.tasks.Tasks;
import br.com.azi.todolist_azi.dtos.CreateTaskDTO;
import br.com.azi.todolist_azi.dtos.UpdateTaskDTO;
import br.com.azi.todolist_azi.exceptions.EmptyTitleTaskException;
import br.com.azi.todolist_azi.exceptions.TaskNotFoundException;
import br.com.azi.todolist_azi.repositories.ITasksRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TasksServiceImplementationTest {

    @Mock
    private ITasksRepository tasksRepository;

    @Autowired
    @InjectMocks
    private TasksServiceImplementation tasksServiceImplementation;

    private Tasks expectedTask;
    private Faker faker;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        faker = new Faker();
        expectedTask = new Tasks(
                1L,
                faker.lorem().characters(10, 20),
                faker.lorem().characters(10, 30),
                false,
                LocalDateTime.now(),
                null
        );
    }

    @Test
    @DisplayName("it should be able to create a new task")
    void createTask() {
        CreateTaskDTO task = new CreateTaskDTO(
                faker.lorem().characters(10,20),
                faker.lorem().characters(0, 30)
        );

        Tasks expectedTask = new Tasks(task);

        when(this.tasksRepository.save(any(Tasks.class))).thenReturn(expectedTask);

        Tasks actualTask = this.tasksServiceImplementation.createTask(task);

        verify(this.tasksRepository, times(1)).save(expectedTask);
        assertEquals(expectedTask, actualTask);
    }

    @Test
    @DisplayName("it should not be able create a new task")
    void createTaskFail() {
        CreateTaskDTO task = new CreateTaskDTO(
                "",
                faker.lorem().characters(0,30)
        );

        EmptyTitleTaskException exception = assertThrows(EmptyTitleTaskException.class, () -> {
            this.tasksServiceImplementation.createTask(task);
        });

        verify(this.tasksRepository, times(0)).save(any(Tasks.class));
        assertEquals("The task title field cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("it should be able to return a list of all tasks")
    void getAllTasks() {
        Tasks firstTask = new Tasks(
                1L,
                faker.lorem().characters(10, 20),
                faker.lorem().characters(10, 30),
                false,
                LocalDateTime.now(),
                null
        );

        Tasks secondTask = new Tasks(
                1L,
                faker.lorem().characters(10, 20),
                faker.lorem().characters(0, 30),
                false,
                LocalDateTime.now(),
                null
        );

        List<Tasks> allTasks = new ArrayList<Tasks>();
        allTasks.add(firstTask);
        allTasks.add(secondTask);

        when(this.tasksRepository.findAll()).thenReturn(allTasks);
        this.tasksServiceImplementation.getAllTasks();

        verify(this.tasksRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("it should be able to return a task by id")
    void getTaskById() {
        when(this.tasksRepository.findById(1L)).thenReturn(Optional.of(expectedTask));

        Tasks actualTask = this.tasksServiceImplementation.getTaskById(1L);

        verify(this.tasksRepository, times(1)).findById(expectedTask.getId());
        assertEquals(expectedTask, actualTask);
    }

    @Test
    @DisplayName("it not should be able to return a task by id")
    void getTaskByIdFail() {
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            this.tasksServiceImplementation.getTaskById(expectedTask.getId());
        });

        verify(this.tasksRepository, times(1)).findById(expectedTask.getId());
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("it should be able to update a task")
    void updateTask() {
        Random random = new Random();
        boolean randomValue = random.nextBoolean();
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(faker.lorem().characters(0, 30)),
                Optional.of(randomValue)
        );

        when(this.tasksRepository.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        when(this.tasksRepository.save(any(Tasks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.tasksServiceImplementation.updateTask(expectedTask.getId(), updateTaskDTO);

        updateTaskDTO.title().ifPresent(expectedTask::setTitle);
        updateTaskDTO.description().ifPresent(expectedTask::setDescription);
        updateTaskDTO.completed().ifPresent(expectedTask::setCompleted);

        verify(this.tasksRepository, times(1)).findById(expectedTask.getId());
        verify(this.tasksRepository, times(1)).save(expectedTask);

        assertEquals(updateTaskDTO.title().orElse(null), expectedTask.getTitle());
        assertEquals(updateTaskDTO.description().orElse(null), expectedTask.getDescription());
        assertEquals(updateTaskDTO.completed().orElse(null), expectedTask.isCompleted());
    }

    @Test
    @DisplayName("it not should be able to update a task")
    void updateTaskFail() {
        Random random = new Random();
        boolean randomValue = random.nextBoolean();

        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(randomValue)
        );

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
           this.tasksServiceImplementation.updateTask(expectedTask.getId(), updateTaskDTO);
        });

        verify(this.tasksRepository, times(1)).findById(expectedTask.getId());
        verify(this.tasksRepository, times(0)).save(expectedTask);
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("it should be able to delete a task")
    void deleteTask() {
        when(this.tasksRepository.findById(expectedTask.getId())).thenReturn(Optional.of(expectedTask));
        doNothing().when(this.tasksRepository).deleteById(expectedTask.getId());

        this.tasksServiceImplementation.deleteTask(expectedTask.getId());

        verify(this.tasksRepository, times(1)).findById(expectedTask.getId());
        verify(this.tasksRepository, times(1)).deleteById(expectedTask.getId());
    }

    @Test
    @DisplayName("it not should be able to delete a task")
    void deleteTaskFail() {
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            this.tasksServiceImplementation.deleteTask(expectedTask.getId());
        });

        verify(this.tasksRepository, times(1)).findById(expectedTask.getId());
        verify(this.tasksRepository, times(0)).deleteById(expectedTask.getId());
        assertEquals("Task not found", exception.getMessage());
    }
}