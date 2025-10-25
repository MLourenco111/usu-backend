package com.fiap.usu.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidAddressException extends BaseBusinessException {
    public InvalidAddressException(List<String> errorCodes) {
        super(errorCodes, "Erro de validação", HttpStatus.BAD_REQUEST);
    }

    public InvalidAddressException(String errorCode) {
        super(errorCode, "Erro de validação", HttpStatus.BAD_REQUEST);
    }
}
