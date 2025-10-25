package com.fiap.usu.dtos.user;

import com.fiap.usu.enums.EnumUserType;

public record UserTypeDto(
        Long id,
        EnumUserType type) {
}
