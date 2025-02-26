package com.example.demo.domain.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserDto;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
