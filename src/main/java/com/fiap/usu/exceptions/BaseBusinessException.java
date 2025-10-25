package com.fiap.usu.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public abstract class BaseBusinessException extends RuntimeException {

    private final List<String> messageKeys;
    private final HttpStatus status;
    private final String title;

    protected BaseBusinessException(String messageKey, String title, HttpStatus status) {
        this(List.of(messageKey), title, status);
    }

    protected BaseBusinessException(List<String> messageKeys, String title, HttpStatus status) {
        super();
        this.messageKeys = messageKeys;
        this.title = title;
        this.status = status;
    }

}
