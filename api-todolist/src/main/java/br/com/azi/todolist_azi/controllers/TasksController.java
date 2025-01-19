package br.com.azi.todolist_azi.controllers;

import br.com.azi.todolist_azi.domain.tasks.Tasks;
import br.com.azi.todolist_azi.dtos.CreateTaskDTO;
import br.com.azi.todolist_azi.dtos.UpdateTaskDTO;
import br.com.azi.todolist_azi.services.implementations.TasksServiceImplementation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class TasksController {

    @Autowired
    private TasksServiceImplementation tasksServiceImplementation;

    @Operation(
            summary = "Create tasks",
            description = "Endpoint responsible to create a new task",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateTaskDTO.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Tasks.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Failed at create a new task")
    })
    @PostMapping("/tasks")
    public ResponseEntity createTask(@RequestBody CreateTaskDTO data) throws Exception {
        Tasks createdTask = this.tasksServiceImplementation.createTask(data);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @Operation(summary = "Get all tasks", description = "Endpoint responsible to get all tasks from database")
    @ApiResponse(
            responseCode = "200",
            description = "All tasks retrieved successfully",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Tasks.class)))}
    )
    @GetMapping("/tasks")
    public ResponseEntity getAllTasks() {
        List<Tasks> tasks = this.tasksServiceImplementation.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @Operation(
            summary = "Get task by id",
            description = "Endpoint responsible to get a task by id",
            parameters = @Parameter(
                    name = "taskId",
                    required = true,
                    description = "Task id to retrieve task on database"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found successfully",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Tasks.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Failed at find task")
    })
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity getTaskById(@PathVariable Long taskId) throws Exception {
        Tasks retrievedTask = this.tasksServiceImplementation.getTaskById(taskId);

        return ResponseEntity.status(HttpStatus.OK).body(retrievedTask);
    }

    @Operation(
            summary = "Update a task",
            description = "Endpoint responsible to update a task",
            parameters = @Parameter(
                    name = "taskId",
                    required = true,
                    description = "Task id to retrive on database and update"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateTaskDTO.class))
            )
    )
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) throws Exception {
        Tasks taskUpdated = this.tasksServiceImplementation.updateTask(taskId, updateTaskDTO);

        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }

    @Operation(
            summary = "Delete a task",
            description = "Endpoint responsible to delete a task",
            parameters = @Parameter(
                    name = "taskId",
                    required = true,
                    description = "Task id to delete on database"
            )
    )
    @ApiResponse(responseCode = "400", description = "Failed at to delete a task")
    @DeleteMapping("/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) throws Exception {
        this.tasksServiceImplementation.deleteTask(taskId);
    }
}
