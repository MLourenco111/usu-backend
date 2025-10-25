package com.fiap.usu.exceptions;

import com.fiap.usu.validations.ValidationMessages;
import org.springframework.http.HttpStatus;

public class InvalidCurrentPasswordException extends BaseBusinessException {

    public InvalidCurrentPasswordException() {
        super(ValidationMessages.CURRENT_PASSWORD_INVALID, "Erro de validação", HttpStatus.BAD_REQUEST);
    }
}