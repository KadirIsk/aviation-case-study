package com.aviation.routing.flight.path.engine.common;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode code;

    protected BaseException(ErrorCode code, String customMessage) {
        super(customMessage != null ? customMessage : code.getDefaultMessage());
        this.code = code;
    }
}