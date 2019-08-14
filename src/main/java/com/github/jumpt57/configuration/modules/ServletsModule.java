package com.github.jumpt57.configuration.modules;

import com.github.jumpt57.filters.HeaderCORSFilter;
import com.github.jumpt57.filters.LogFilter;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.guice.EndpointsModule;
import org.reflections.Reflections;

import java.util.Set;

public class ServletsModule extends EndpointsModule {

    @Override
    public void configureServlets() {

        super.configureServlets();

        filter("/_ah/api/*").through(LogFilter.class);
        filter("/_ah/api/*").through(HeaderCORSFilter.class);

        Reflections reflections = new Reflections("com.github");
        Set<Class<?>> endpoints = reflections.getTypesAnnotatedWith(Api.class);
        configureEndpoints("/_ah/api/*", endpoints);
    }

}
