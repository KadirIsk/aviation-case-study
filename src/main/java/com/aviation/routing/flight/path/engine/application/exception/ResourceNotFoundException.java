package com.aviation.routing.flight.path.engine.application.exception;

import com.aviation.routing.flight.path.engine.common.BaseException;
import com.aviation.routing.flight.path.engine.common.ErrorCode;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}