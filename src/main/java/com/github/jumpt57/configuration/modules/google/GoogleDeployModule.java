package com.github.jumpt57.configuration.modules.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
public class GoogleDeployModule extends GoogleModule  {

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
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }

    @Provides
    @Singleton
    public GoogleCredential googleCredential() throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(new File("WEB-INF/credential.json")));
    }

}
