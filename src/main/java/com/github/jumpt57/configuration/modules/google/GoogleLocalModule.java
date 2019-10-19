package com.github.jumpt57.configuration.modules.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Slf4j
public class GoogleLocalModule extends GoogleModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public NetHttpTransport netHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Provides
    @Singleton
    public Storage storage() throws IOException {
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(getCredentialAsInputStream()))
                .build()
                .getService();
    }

    @Provides
    @Singleton
    public GoogleCredential googleCredential() throws IOException {
        return GoogleCredential.fromStream(getCredentialAsInputStream());
    }

    private InputStream getCredentialAsInputStream() {
        return Optional.ofNullable(GoogleLocalModule.class
                .getClassLoader()
                .getResourceAsStream("configuration/local/credential.json"))
                .orElseThrow(() -> new IllegalStateException("Impossible to get configuration/local/credential.json"));
    }

}
