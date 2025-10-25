package com.fiap.usu.dtos;

import com.fiap.usu.enums.EnumPhoneType;

public record PhoneDto(
        Long id,
        EnumPhoneType type,
        String number) {
}
