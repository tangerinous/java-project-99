package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;

import java.util.Date;
import java.util.Set;

import static jakarta.persistence.GenerationType.AUTO;
import static jakarta.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.FetchMode.JOIN;

@Entity
@Getter
@Setter
@Table(name = "tasks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "taskStatus", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private TaskStatus taskStatus;

    @ManyToMany
    @Fetch(JOIN)
    private Set<Label> labels;

    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;

    private String description;

    private int index;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;
}

