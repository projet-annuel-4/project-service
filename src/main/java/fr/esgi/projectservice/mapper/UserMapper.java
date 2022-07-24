package fr.esgi.projectservice.mapper;


import fr.esgi.projectservice.data.entity.UserEntity;
import fr.esgi.projectservice.dto.UserEvent;
import fr.esgi.projectservice.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse convertToResponseDto(UserEntity user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserEntity convertToEntity(UserEvent user) {
        return modelMapper.map(user, UserEntity.class);
    }
}
