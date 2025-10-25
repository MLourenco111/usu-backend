package com.fiap.usu.exceptions;

import com.fiap.usu.validations.ValidationMessages;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseBusinessException {

    public InvalidCredentialsException() {
        super(ValidationMessages.INVALID_CREDENTIALS, "Credenciais Inválidas", HttpStatus.UNAUTHORIZED);
    }
}
