package com.fiap.usu.dtos.user;

import com.fiap.usu.dtos.PhoneDto;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record UserDto(
        Long id,
        String name,
        String email,
        String login,
        String createdAt,
        String updatedAt,
        String document,
        boolean status,
        LocalDate birthday,
        Set<PhoneDto> phones,
        Set<UserAddressDto> addresses,
        Set<UserTypeDto> types) {
}
