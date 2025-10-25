package com.fiap.usu.dtos.user;

import com.fiap.usu.enums.EnumPhoneType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreatePhoneDto(
        @NotNull EnumPhoneType type,
        @NotBlank String number) {
}
