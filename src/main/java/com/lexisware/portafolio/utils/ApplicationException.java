package com.lexisware.portafolio.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepción general de la aplicación que incluye un HttpStatus
 */
@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApplicationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApplicationException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
