package hexlet.code.controller;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.TaskQueryDto;
import hexlet.code.model.Label;
import hexlet.code.model.QTask;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "javainuseapi")
@RequestMapping("${base-url:/api}" + TASK_CONTROLLER_PATH)
public class TaskController {

    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";

    private static final String ONLY_AUTHOR_BY_ID = """
                @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
            """;

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Operation(summary = "Get Tasks by Predicate")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class))))
    )
    @GetMapping
    public ResponseEntity<Iterable<TaskDto>> getFilteredTasks(
            @Parameter(description = "Predicate based on query params")
            TaskQueryDto predicate, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(predicate.getTitleCont())) {
            builder.and(QTask.task.description.eq(predicate.getTitleCont()));
        }
        if (Objects.nonNull(predicate.getAssigneeId())) {
            builder.and(QTask.task.assignee.id.eq(predicate.getAssigneeId()));
        }
        if (Objects.nonNull(predicate.getStatus())) {
            builder.and(QTask.task.taskStatus.slug.eq(predicate.getStatus()));
        }
        if (Objects.nonNull(predicate.getLabelId())) {
            builder.and(QTask.task.labels.any().id.eq(predicate.getLabelId()));
        }
        Iterable<Task> tasks = taskRepository.findAll(builder);
        List<TaskDto> result = StreamSupport.stream(tasks.spliterator(), false)
                .map(TaskController::mapTaskDto).collect(Collectors.toList());
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    private static TaskDto mapTaskDto(Task t) {
        Set<Long> labelsIds = t.getLabels().stream().map(Label::getId).collect(Collectors.toSet());

        TaskDto taskDto = new TaskDto();
        taskDto.setId(t.getId());
        taskDto.setTitle(t.getName());
        taskDto.setContent(t.getDescription());
        taskDto.setIndex(t.getId());
        if (t.getAssignee() != null) {
            taskDto.setAssigneeId(t.getAssignee().getId());
        }
        if (t.getTaskStatus() != null) {
            taskDto.setStatus(t.getTaskStatus().getName());
        }
        taskDto.setTaskLabelIds(labelsIds);
        return taskDto;
    }

    @Operation(summary = "Get Task by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @GetMapping(ID)
    public Task getById(@PathVariable final Long id) {
        return taskRepository.findById(id).get();
    }

    @Operation(summary = "Create new Task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Task createNewTask(@RequestBody @Valid final TaskDto dto) {
        return taskService.createNewTask(dto);
    }

    @Operation(summary = "Update Task")
    @ApiResponse(responseCode = "200", description = "Task updated")
    @PutMapping(ID)
    public Task updateTask(@PathVariable final Long id,
                           @Parameter(schema = @Schema(implementation = TaskDto.class))
                           @RequestBody @Valid final TaskDto dto) {
        return taskService.updateTask(id, dto);
    }

    @Operation(summary = "Delete Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task with that id not found")
    })
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    public ResponseEntity deleteTask(@PathVariable final Long id) {
        taskRepository.deleteById(id);
        return ResponseEntity.status(204).build();
    }


}
