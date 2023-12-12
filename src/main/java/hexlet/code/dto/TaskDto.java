package hexlet.code.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TaskDto {
    private @NotBlank @Size(min = 3, max = 1000) String title;

    private String content;

    @JsonProperty("assignee_id")
    private Long assigneeId;

    private @NotNull String status;

    private Set<String> labels;

}
