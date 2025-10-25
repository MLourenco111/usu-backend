package com.fiap.usu.mappers;

import com.fiap.usu.dtos.user.UserCreateDto;
import com.fiap.usu.dtos.user.UserDto;
import com.fiap.usu.dtos.user.UserUpdateDto;
import com.fiap.usu.entities.User;
import com.fiap.usu.utils.DateUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Collectors;

public class UserMapper {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User mapPersistenceEntity(UserCreateDto dto) {
        User entity = new User();
        entity.setName(dto.name().toLowerCase());
        entity.setEmail(dto.email());
        entity.setLogin(dto.login());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        entity.setDocument(dto.document());
        entity.setBirthday(dto.birthday());
        entity.setStatus(true);

        return entity;
    }

    public static UserDto mapUserDto(User user) {
        return UserDto.builder().id(user.getId())
                .createdAt(DateUtils.formatInstant(user.getCreatedAt()))
                .updatedAt(DateUtils.formatInstant(user.getUpdatedAt()))
                .name(user.getName()).email(user.getEmail())
                .login(user.getLogin()).status(user.isStatus())
                .birthday(user.getBirthday()).document(user.getDocument())
                .phones(user.getPhones().stream().map(PhoneMapper::mapPhoneDto).collect(Collectors.toSet()))
                .types(user.getTypes().stream().map(UserTypeMapper::mapUserTypeDto).collect(Collectors.toSet()))
                .addresses(user.getAddresses().stream().map(UserAddressMapper::mapUserAddressDto).collect(Collectors.toSet()))
                .build();
    }

    public static void mapUpdateEntity(UserUpdateDto dto, User user) {
        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.document() != null) user.setDocument(dto.document());
        if (dto.birthday() != null) user.setBirthday(dto.birthday());
    }
}
