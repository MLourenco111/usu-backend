package com.fiap.usu.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseBusinessException {

    public ResourceNotFoundException(String messageKey) {
        super(messageKey, "Recurso não encontrado", HttpStatus.NOT_FOUND);

    }
}