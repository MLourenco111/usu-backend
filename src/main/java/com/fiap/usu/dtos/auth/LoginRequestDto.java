package com.fiap.usu.dtos.auth;

public record LoginRequestDto(
        String identifier,
        String password) {
}
