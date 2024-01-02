package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    @Override
    public Task createNewTask(final TaskDto dto) {
        final Task newTask = fromDto(dto);
        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(final long id, final TaskDto dto) {
        final Task task = taskRepository.findById(id).get();
        merge(task, dto);
        return taskRepository.save(task);
    }

    private void merge(final Task task, final TaskDto taskDto) {
        final Task newTask = fromDto(taskDto);
        task.setName(newTask.getName());
        task.setDescription(newTask.getDescription());
        task.setAssignee(newTask.getAssignee());
        task.setTaskStatus(newTask.getTaskStatus());
        task.setLabels(newTask.getLabels());
    }

    private Task fromDto(final TaskDto dto) {
        final User executor = Optional.ofNullable(dto.getAssigneeId())
                .flatMap(userRepository::findById)
                .orElse(null);

        final TaskStatus taskStatus = taskStatusRepository.findBySlug(dto.getStatus())
                .orElse(null);

        final Set<Label> labels = Optional.ofNullable(dto.getTaskLabelIds())
                .orElse(Set.of())
                .stream()
                .filter(Objects::nonNull)
                .map(l -> new Label(l))
                .collect(Collectors.toSet());

        return Task.builder()
                .assignee(executor)
                .taskStatus(taskStatus)
                .labels(labels)
                .name(dto.getName() != null ? dto.getName() : dto.getTitle())
                .description(dto.getContent())
                .build();
    }
}


