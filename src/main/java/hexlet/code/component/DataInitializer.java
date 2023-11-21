package hexlet.code.component;

import hexlet.code.dto.UserDto;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String email = "hexlet@example.com";
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setPassword("qwerty");
            userDto.setFirstName("David");
            userDto.setLastName("David");
            userService.createNewUser(userDto);
        }

        List<TaskStatus> taskStatuses = taskStatusRepository.findAll();
        if (taskStatuses.isEmpty()) {
            var statusses = List.of(
                    TaskStatus.builder().name("Draft").slug("draft").build(),
                    TaskStatus.builder().name("ToReview").slug("to_review").build(),
                    TaskStatus.builder().name("ToBeFixed").slug("to_be_fixed").build(),
                    TaskStatus.builder().name("ToPublish").slug("to_publish").build(),
                    TaskStatus.builder().name("Published").slug("published").build()
            );
            taskStatusRepository.saveAll(statusses);
        }

        List<Label> labels = labelRepository.findAll();
        if (labels.isEmpty()) {
            var newLabels = List.of(
                    Label.builder().name("feature").build(),
                    Label.builder().name("bug").build()
                    );
            labelRepository.saveAll(newLabels);
        }
    }
}
