package com.fiap.usu.dtos.user;

import com.fiap.usu.dtos.AddressDto;
import com.fiap.usu.enums.EnumAddressType;
import lombok.Builder;

@Builder
public record UserAddressDto(
        EnumAddressType addressType,
        String complement,
        Boolean primary,
        AddressDto address) {
}
