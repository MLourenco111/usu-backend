package com.fiap.usu.dtos.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record UserCreateDto(
        @NotBlank String name,
        @NotBlank @Email
        String email,
        @NotBlank String login,
        @NotBlank String document,
        @NotBlank @Size(min = 6, max = 50)
        String password,
        LocalDate birthday,
        @Valid Set<UserCreatePhoneDto> phones,
        @Valid Set<UserCreateAddressDto> addresses,
        @NotNull Set<Long> idTypes) {
}
