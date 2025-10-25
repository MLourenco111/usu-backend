package com.fiap.usu.handlers;

import com.fiap.usu.exceptions.BaseBusinessException;
import com.fiap.usu.validations.ValidationMessages;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Trata exceções de negócio baseadas em BaseBusinessException
     */
    @ExceptionHandler(BaseBusinessException.class)
    public ProblemDetail handleBusinessException(BaseBusinessException ex, HttpServletRequest request, Locale locale) {

        List<String> messages = ex.getMessageKeys().stream().map(key -> messageSource.getMessage(key, null, locale)).toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setTitle(ex.getTitle());
        problemDetail.setDetail(String.join("; ", messages));
        problemDetail.setType(URI.create("https://example.com/problems/business-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return problemDetail;
    }

    /**
     * Validações de DTO
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String message = messageSource.getMessage(error, locale);
            errors.put(error.getField(), message);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Erro de validação");
        problemDetail.setDetail("Um ou mais campos estão inválidos");
        problemDetail.setType(URI.create("https://example.com/problems/validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    /**
     * Exceções genéricas
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Erro interno do servidor");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setType(URI.create("https://example.com/problems/internal-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return problemDetail;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleGenericException(AuthorizationDeniedException ex, HttpServletRequest request, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problemDetail.setTitle("Access Denied");
        problemDetail.setDetail(messageSource.getMessage(ValidationMessages.FORBIDDEN, null, locale));
        problemDetail.setStatus(HttpStatus.FORBIDDEN);
        problemDetail.setType(URI.create("https://example.com/problems/internal-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("path", request.getRequestURI());

        return problemDetail;
    }

}
