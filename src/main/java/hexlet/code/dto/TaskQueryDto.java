package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskQueryDto {
    private String titleCont;

    private Long assigneeId;

    private String status;

    private Long labelId;

}
