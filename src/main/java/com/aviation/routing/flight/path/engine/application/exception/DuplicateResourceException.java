package com.aviation.routing.flight.path.engine.application.exception;

import com.aviation.routing.flight.path.engine.common.exception.BaseException;
import com.aviation.routing.flight.path.engine.common.exception.ErrorCode;

public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(ErrorCode code, String message) {
        super(code, message);
    }
}