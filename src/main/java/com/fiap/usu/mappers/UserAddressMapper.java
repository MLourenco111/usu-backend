package com.fiap.usu.mappers;

import com.fiap.usu.dtos.AddressDto;
import com.fiap.usu.dtos.user.UserAddressDto;
import com.fiap.usu.entities.UserAddress;

public class UserAddressMapper {

    public static UserAddressDto mapUserAddressDto(UserAddress userAddress) {
        AddressDto addressDto = new AddressDto(userAddress.getAddress().getId(), userAddress.getAddress().getStreet(),
                userAddress.getAddress().getNumber(), userAddress.getAddress().getCity(),
                userAddress.getAddress().getState(), userAddress.getAddress().getState(),
                userAddress.getAddress().getZipCode());

        return new UserAddressDto(userAddress.getAddressType(), userAddress.getComplement(), userAddress.getPrimary(), addressDto);
    }

}
