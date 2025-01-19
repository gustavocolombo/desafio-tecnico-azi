package br.com.azi.todolist_azi.controllers;

import br.com.azi.todolist_azi.domain.tasks.Tasks;
import br.com.azi.todolist_azi.dtos.CreateTaskDTO;
import br.com.azi.todolist_azi.dtos.UpdateTaskDTO;
import br.com.azi.todolist_azi.exceptions.EmptyTitleTaskException;
import br.com.azi.todolist_azi.exceptions.TaskNotFoundException;
import br.com.azi.todolist_azi.services.implementations.TasksServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(TasksController.class)
@ActiveProfiles("test")
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TasksServiceImplementation tasksServiceImplementation;

    @Autowired
    private ObjectMapper objectMapper;

    private Faker faker;
    private Tasks task;
    private CreateTaskDTO createTaskDTO;

    @BeforeEach
    void setup() {
        faker = new Faker();
        createTaskDTO = new CreateTaskDTO(
                faker.lorem().characters(10, 20),
                faker.lorem().characters(0, 30)
        );
        task = new Tasks(
                1L,
                createTaskDTO.title(),
                createTaskDTO.description(),
                false,
                LocalDateTime.now(),
                null
        );
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JSR310Module());
    }

    @Test
    @DisplayName("it should be able to create a new task")
    void createTask() throws Exception {
        when(this.tasksServiceImplementation.createTask(any(CreateTaskDTO.class))).thenReturn(task);

        this.tasksServiceImplementation.createTask(any(CreateTaskDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createTaskDTO.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createTaskDTO.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(task.isCompleted()))
                .andDo(print());
    }

    @Test
    @DisplayName("it not should be able to create a new task")
    void createTaskFail() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO(
                "",
                faker.lorem().characters(0, 30)
        );

        doThrow(new EmptyTitleTaskException("The task title field cannot be empty"))
                .when(this.tasksServiceImplementation).createTask(any(CreateTaskDTO.class));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTaskDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The task title field cannot be empty"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(400));
    }

    @Test
    @DisplayName("it should be able to get all tasks from database")
    void getAllTasks() throws Exception {
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

        when(this.tasksServiceImplementation.getAllTasks()).thenReturn(allTasks);

        this.tasksServiceImplementation.getAllTasks();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("it should be able to return a task by id")
    void getTaskById() throws Exception {
        when(this.tasksServiceImplementation.getTaskById(task.getId())).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/{taskId}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createTaskDTO.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createTaskDTO.description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(task.isCompleted()))
                .andDo(print());
    }

    @Test
    @DisplayName("it not should be able to return a task by id")
    void getTaskByIdFail() throws Exception {
        doThrow(new TaskNotFoundException("Task not found"))
                .when(this.tasksServiceImplementation).getTaskById(task.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/{taskId}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("taskId", "1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Task not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404))
                .andDo(print());
    }

    @Test
    @DisplayName("it should be able to update a task")
    void updateTask() throws Exception {
        Random random = new Random();
        boolean randomValue = random.nextBoolean();

        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(randomValue)
        );

        Tasks updatedTask = new Tasks(
            task.getId(),
            updateTaskDTO.title().orElse(task.getTitle()),
            updateTaskDTO.description().orElse(task.getDescription()),
            updateTaskDTO.completed().orElse(task.isCompleted()) ,
            task.getCreatedAt(),
            LocalDateTime.now()
        );

        when(this.tasksServiceImplementation.updateTask(task.getId(), updateTaskDTO)).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/{taskId}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(task.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(updatedTask.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedTask.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(updatedTask.isCompleted()));
    }

    @Test
    @DisplayName("it not should be able to update a task")
    void updateTaskFail() throws Exception {
        Random random = new Random();
        boolean randomValue = random.nextBoolean();

        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(faker.lorem().characters(10, 20)),
                Optional.of(randomValue)
        );

        doThrow(new TaskNotFoundException("Task not found"))
                .when(this.tasksServiceImplementation).updateTask(task.getId(), updateTaskDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/{taskId}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTaskDTO)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Task not found"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404));
    }

    @Test
    @DisplayName("it should be able to delete a task")
    void deleteTask() throws Exception {
        doNothing().when(this.tasksServiceImplementation).deleteTask(task.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("taskId", "1"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("it not should be able to delete a task")
    void deleteTaskFail() throws Exception {
        doThrow(new TaskNotFoundException("Task not found"))
                .when(this.tasksServiceImplementation).deleteTask(task.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("taskId", "1"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Task not found"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(404));
    }
}