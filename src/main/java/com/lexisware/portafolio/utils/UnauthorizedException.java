package com.lexisware.portafolio.utils;

// Excepción para acceso no autorizado (401)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
