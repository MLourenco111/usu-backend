package com.fiap.usu.dtos.user;

import com.fiap.usu.enums.EnumAddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateAddressDto(
        @NotNull EnumAddressType addressType,
        @NotBlank String street,
        @NotBlank String number,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String country,
        @NotBlank String zipCode,
        @NotNull boolean primary,
        String complement) {
}
