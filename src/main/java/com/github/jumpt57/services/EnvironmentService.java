package com.github.jumpt57.services;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class EnvironmentService {

    private static final String ENVIRONMENT = "environment";

    private String env;

    public EnvironmentService() {
        env = System.getProperty(ENVIRONMENT);
    }

    public String get() {
        return env;
    }

}
