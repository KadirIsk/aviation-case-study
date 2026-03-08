package com.aviation.routing.flight.path.engine.infrastructure.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProfileConstants {
    public static final String LOCAL = "local";
    public static final String TEST = "test";
    public static final String PROD = "prod";
    
    public static final String NOT_LOCAL = "!" + LOCAL;
    public static final String NOT_TEST = "!" + TEST;
    public static final String NOT_PROD = "!" + PROD;
}
