package com.fiap.usu.exceptions;

import com.fiap.usu.validations.ValidationMessages;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BaseBusinessException {

    public InvalidPasswordException() {
        super(ValidationMessages.PASSWORD_INVALID, "Senha inválida", HttpStatus.BAD_REQUEST);
    }
}
