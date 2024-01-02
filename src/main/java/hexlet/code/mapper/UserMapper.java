package hexlet.code.mapper;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@AllArgsConstructor
@Service
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User map(UserDto userDto) {
        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(
                passwordEncoder.encode(
                        userDto.getPassword()
                )
        );
        return user;
    }

    public UserDto map(User u) {
        final UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        return dto;
    }

    public void update(UserDto userDto, User userToUpdate) {
        if(userDto.getEmail() != null){
            userToUpdate.setEmail(userDto.getEmail());
        }

        if(userDto.getFirstName() != null){
            userToUpdate.setFirstName(userDto.getFirstName());
        }

        if(userDto.getLastName() != null){
            userToUpdate.setLastName(userDto.getLastName());
        }

        if(userDto.getPassword() != null){
            userToUpdate.setPassword(
                    passwordEncoder.encode(
                            userDto.getPassword()
                    )
            );
        }

        userToUpdate.setUpdatedAt(LocalDate.now());
    }

}
