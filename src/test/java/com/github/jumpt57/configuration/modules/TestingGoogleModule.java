package com.github.jumpt57.configuration.modules;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class TestingGoogleModule extends AbstractModule {

    private static final String CREDENTIAL_JSON = "credential.json";

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public Storage storage() throws IOException {

        InputStream inputStream = Optional.ofNullable(TestingGoogleModule.class.getClassLoader().getResourceAsStream(CREDENTIAL_JSON))
                .orElseThrow(() -> new IllegalStateException("Impossible to get credential file"));

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
                .getService();
    }

}
