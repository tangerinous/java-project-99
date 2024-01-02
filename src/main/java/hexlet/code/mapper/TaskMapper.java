package hexlet.code.mapper;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    public TaskDto mapTask(Task t) {
        Set<Long> labelsIds = t.getLabels().stream().map(Label::getId).collect(Collectors.toSet());

        TaskDto taskDto = new TaskDto();
        taskDto.setId(t.getId());
        taskDto.setTitle(t.getName());
        taskDto.setName(t.getName());
        taskDto.setContent(t.getDescription());
        taskDto.setIndex(t.getId());
        if (t.getAssignee() != null) {
            taskDto.setAssigneeId(t.getAssignee().getId());
        }
        if (t.getTaskStatus() != null) {
            taskDto.setStatus(t.getTaskStatus().getSlug());
        }
        taskDto.setTaskLabelIds(labelsIds);
        return taskDto;
    }
}
