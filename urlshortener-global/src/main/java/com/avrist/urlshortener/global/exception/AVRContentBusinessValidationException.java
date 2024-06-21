package com.avrist.urlshortener.global.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class AVRContentBusinessValidationException extends AVRBusinessValidationException {
    private static final long serialVersionUID = 1576455084324353532L;

    public AVRContentBusinessValidationException(String responseCode, String responseMessage, HttpStatus httpStatus, List<String> error) {
        super(responseCode, responseMessage, httpStatus, error);
    }

    public AVRContentBusinessValidationException(String responseCode, String responseMessage, HttpStatus httpStatus, List<String> error, String exceptionMsg, Throwable cause) {
        super(responseCode, responseMessage, httpStatus, error, exceptionMsg, cause);
    }
}
