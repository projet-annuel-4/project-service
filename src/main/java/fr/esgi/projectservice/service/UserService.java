package fr.esgi.projectservice.service;


import fr.esgi.projectservice.data.entity.UserEntity;
import fr.esgi.projectservice.data.repository.UserRepository;
import fr.esgi.projectservice.dto.UserEvent;
import fr.esgi.projectservice.dto.UserResponse;
import fr.esgi.projectservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserEntity createUser(UserEvent userEvent) {
        var user = userMapper.convertToEntity(userEvent);
        return userRepository.save(user);
    }

    public UserResponse getUserById(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User", "id");
        }
        return userMapper.convertToResponseDto(user.get());
    }

    public UserResponse getUserByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User", "email");
        }
        return userMapper.convertToResponseDto(user.get());
    }
}
