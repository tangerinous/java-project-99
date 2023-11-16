package hexlet.code.dto;

import java.util.Set;
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
    private @NotBlank @Size(min = 3, max = 1000) String name;

    private String description;

    private Long executorId;

    private @NotNull Long taskStatusId;

    private Set<Long> labelIds;

}
