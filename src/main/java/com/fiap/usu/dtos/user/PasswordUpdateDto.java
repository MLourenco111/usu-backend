package com.fiap.usu.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDto(
        @NotBlank @Email String email,
        @NotBlank String currentPassword,
        @NotBlank @Size(min = 6, max = 50) String newPassword) {
}
