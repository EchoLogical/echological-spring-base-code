package com.avrist.urlshortener.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

public class AVRBusinessValidationException extends RuntimeException {
    private static final long serialVersionUID = 1576455084324353532L;
    @Getter
    private final String responseCode;
    @Getter
    private final String responseMessage;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final List<String> error;


    public AVRBusinessValidationException(
            String responseCode,
            String responseMessage,
            HttpStatus httpStatus,
            List<String> error) {
        super(String.format("%s;%s;%s;%s",
                responseCode,
                responseMessage,
                httpStatus.value(),
                String.join("|", error)));
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
        this.error = error;
    }

    public AVRBusinessValidationException(
            String responseCode,
            String responseMessage,
            HttpStatus httpStatus,
            List<String> error,
            String exceptionMsg,
            Throwable cause) {
        super(exceptionMsg, cause);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
        this.error = error;
    }
}
