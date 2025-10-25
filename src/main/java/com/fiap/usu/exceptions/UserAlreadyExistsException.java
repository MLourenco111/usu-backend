package com.fiap.usu.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseBusinessException {

    public UserAlreadyExistsException(String messageKey) {
        super(messageKey, "Erro de validação", HttpStatus.BAD_REQUEST);
    }
}
