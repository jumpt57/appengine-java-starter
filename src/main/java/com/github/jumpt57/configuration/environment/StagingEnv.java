package com.github.jumpt57.configuration.environment;

import java.util.Collections;
import java.util.List;

public class StagingEnv extends Configuration {

    @Override
    public List<String> getAuthDomains() {
        return Collections.singletonList(
                "gmail.com"
        );
    }

    @Override
    public List<String> getClientIds() {
        return Collections.singletonList(
                "292824132082.apps.googleusercontent.com"
        );
    }

}
