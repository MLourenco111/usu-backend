package com.fiap.usu.dtos.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import java.util.Set;

public record UserUpdateDto(
        String name,
        @Email String email,
        String login,
        String document,
        LocalDate birthday,
        Set<Long> idsTypes,
        @Valid Set<UserUpdatePhoneDto> phones,
        @Valid Set<UserUpdateAddressDto> addresses) {
}
