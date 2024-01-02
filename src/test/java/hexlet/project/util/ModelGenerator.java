package hexlet.project.util;

import hexlet.code.dto.UserDto;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;

public class ModelGenerator {
    private Model<Task> taskModel;
    private Model<User> userModel;
    private Model<UserDto> userDtoModel;

    public Model<Task> getTaskModel() {
        return taskModel;
    }

    public Model<User> getUserModel() {
        return userModel;
    }
    public Model<UserDto> getUserDtoModel() {
        return userDtoModel;
    }

    public ModelGenerator() {
        var faker = new Faker();

        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getPassword), () -> "password")
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .toModel();

        userDtoModel = Instancio.of(UserDto.class)
                .ignore(Select.field(UserDto::getId))
                .supply(Select.field(UserDto::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(UserDto::getPassword), () -> "password")
                .supply(Select.field(UserDto::getEmail), () -> faker.internet().emailAddress())
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.gameOfThrones().house())
                .supply(Select.field(Task::getDescription), () -> faker.gameOfThrones().quote())
                .toModel();
    }
}
