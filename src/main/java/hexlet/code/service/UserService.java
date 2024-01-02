package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsManager {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    @Override
    public void createUser(UserDetails userData) {
        createAndReturnUser((UserDto) userData);
    }

    public User createAndReturnUser(UserDto data) {
        var user = userMapper.map(data);
        return userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userData) {
        // updateAndReturnUser((UserDTO) userData);
    }

    public User updateAndReturnUser(Long id, UserDto data) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userMapper.update(data, user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }
}
