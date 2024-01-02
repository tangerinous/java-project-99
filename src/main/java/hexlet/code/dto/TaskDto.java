package hexlet.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TaskDto {
    private Long id;

    private String title;

    private String content;

    private String name;

    private long index;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private String status;

    private Set<Long> taskLabelIds;

}
