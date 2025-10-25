package com.fiap.usu.dtos;

public record AddressDto(
        Long id,
        String street,
        String number,
        String city,
        String state,
        String country,
        String zipCode) {
}
