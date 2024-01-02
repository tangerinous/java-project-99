package hexlet.code.controller.api;

import hexlet.code.dto.UserDto;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserRepository userRepository;

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.name
        """;


    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDto>> index() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid final UserDto data) {
        var user = userService.createAndReturnUser(data);
        return userMapper.map(user);
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto show(@PathVariable final Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Not Found: " + id));
        return userMapper.map(user);
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable final long id, @RequestBody @Valid UserDto data) {
        var user = userService.updateAndReturnUser(id, data);
        return userMapper.map(user);
    }

    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long id) {
        userRepository.deleteById(id);
    }
}
