package com.example.demo.domain.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserDto;

@Component
@Mapper(componentModel = "spring")

public interface UserMapper {
    // UserMapper Instance = Mappers.getMapper(UserMapper.class);

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
