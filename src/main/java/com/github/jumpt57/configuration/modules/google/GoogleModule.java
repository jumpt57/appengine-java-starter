package com.github.jumpt57.configuration.modules.google;

import com.google.cloud.storage.Storage;
import com.google.inject.AbstractModule;

import java.io.IOException;

abstract class GoogleModule extends AbstractModule {

    abstract Storage storage() throws IOException;

}
