package com.aviation.routing.flight.path.engine.application.exception;

import com.aviation.routing.flight.path.engine.common.BaseException;
import com.aviation.routing.flight.path.engine.common.ErrorCode;

public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(ErrorCode code, String message) {
        super(code, message);
    }
}