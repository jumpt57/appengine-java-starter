package com.github.jumpt57.configuration.modules.google;

public class GoogleModuleFactory {

    private static final String ENVIRONMENT = "environment";
    private static final String LOCAL = "local";

    public static GoogleModule get() {
        if (LOCAL.equals(System.getProperty(ENVIRONMENT))) {
            return new GoogleLocalModule();
        }
        return new GoogleDeployModule();
    }

}
