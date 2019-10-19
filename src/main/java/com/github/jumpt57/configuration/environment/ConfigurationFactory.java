package com.github.jumpt57.configuration.environment;

public class ConfigurationFactory {

    private static final String ENVIRONMENT = "environment";
    private static final String LOCAL = "local";
    private static final String DEV = "dev";
    private static final String STAGING = "staging";
    private static final String PROD = "prod";

    public static Configuration get() {
        switch (System.getProperty(ENVIRONMENT)) {
            case STAGING:
                return new StagingEnv();
            case PROD:
                return new ProdEnv();
            case LOCAL:
            case DEV:
            default:
                return new DevEnv();
        }
    }

}
