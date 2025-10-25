package com.fiap.usu.mappers;

import com.fiap.usu.dtos.PhoneDto;
import com.fiap.usu.entities.Phone;

public class PhoneMapper {

    public static PhoneDto mapPhoneDto(Phone entity) {
        return new PhoneDto(entity.getId(), entity.getType(), entity.getNumber());
    }
}
