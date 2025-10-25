package com.fiap.usu.mappers;

import com.fiap.usu.dtos.user.UserTypeDto;
import com.fiap.usu.entities.UserType;

public class UserTypeMapper {

    public static UserTypeDto mapUserTypeDto(UserType entity) {
        return new UserTypeDto(entity.getId(), entity.getType());
    }
}

