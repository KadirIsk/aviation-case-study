package com.aviation.routing.flight.path.engine.application.exception;

import com.aviation.routing.flight.path.engine.common.exception.BaseException;
import com.aviation.routing.flight.path.engine.common.exception.ErrorCode;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}