package com.aviation.routing.flight.path.engine.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    LOC_NF_001("Location not found with the provided identifier."),
    LOC_DUP_001("A location with this name or code already exists."),

    TRN_NF_001("Transportation not found."),
    TRN_DUP_001("A transportation link between these locations already exists."),

    RTE_BIZ_001("Route exceeds the maximum allowed transportations (3)."),
    RTE_BIZ_002("Route must contain exactly one flight."),
    RTE_BIZ_003("Transfers before/after flight limit exceeded."),

    SYS_VAL_001("Request validation failed."),
    SYS_ERR_500("An unexpected internal system error occurred."),

    SYS_DB_CONSTRAINT_001("Database constraint violation: Duplicate or invalid relationship detected.");

    private final String defaultMessage;
}