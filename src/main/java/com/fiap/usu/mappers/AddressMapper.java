package com.fiap.usu.mappers;

import com.fiap.usu.dtos.user.UserCreateAddressDto;
import com.fiap.usu.dtos.user.UserUpdateAddressDto;
import com.fiap.usu.entities.Address;
import com.fiap.usu.utils.AddressUtils;
import com.fiap.usu.utils.StringUtils;

public class AddressMapper {

    public static Address mapPersistenceEntity(UserCreateAddressDto dto) {
        Address entity = new Address();
        entity.setZipCode(AddressUtils.normalize(dto.zipCode()));
        entity.setNumber(StringUtils.safeUpper(dto.number()));
        entity.setStreet(StringUtils.capitalizeWords(dto.street()));
        entity.setState(StringUtils.safeUpper(dto.state()));
        entity.setCountry(StringUtils.capitalizeWords(dto.country()));
        entity.setCity(StringUtils.capitalizeWords(dto.city()));
        return entity;
    }

    public static Address mapUpdateEntity(UserUpdateAddressDto dto) {
        Address entity = new Address();
        entity.setZipCode(AddressUtils.normalize(dto.zipCode()));
        entity.setNumber(StringUtils.safeUpper(dto.number()));
        entity.setStreet(StringUtils.capitalizeWords(dto.street()));
        entity.setState(StringUtils.safeUpper(dto.state()));
        entity.setCountry(StringUtils.capitalizeWords(dto.country()));
        entity.setCity(StringUtils.capitalizeWords(dto.city()));
        return entity;
    }
}
