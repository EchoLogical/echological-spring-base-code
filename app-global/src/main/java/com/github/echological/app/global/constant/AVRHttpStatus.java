package com.github.echological.app.global.constant;

import lombok.Getter;

public enum AVRHttpStatus {

    SUCCESS("SUCCESS", "AVR-A0001"),
    OK("OK", "AVR-A0002"),
    REGISTERED("REGISTERED", "AVR-A0003"),
    INTERRUPTED("INTERRUPTED", "AVR-X0002"),
    FAILED("FAILED", "AVR-X0003"),
    CONNECTION_FAILED("CONNECTION_FAILED", "AVR-X0004"),
    SERVICE_NOT_FOUND("SERVICE_NOT_FOUND", "AVR-X0005"),
    DATA_NOT_FOUND("NOT_FOUND", "AVR-X0006"),
    INVALID_ARGUMENT("INVALID_ARGUMENT", "AVR-X0007"),
    MESSAGE_NOT_READABLE("MESSAGE_NOT_READABLE", "AVR-X0008"),
    NO_HANDLER_FOUND("NO_HANDLER_FOUND", "AVR-X0009"),
    UNAUTHORIZED("UNAUTHORIZED", "AVR-X0010"),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "AVR-X0011"),
    ERROR("ERROR", "AVR-X0001");

    @Getter
    String status;
    @Getter
    String code;

    AVRHttpStatus(String status, String code) {
        this.status = status;
        this.code = code;
    }
}
