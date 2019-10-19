package com.github.jumpt57.configuration.modules;

import com.github.jumpt57.filters.HeaderCORSFilter;
import com.github.jumpt57.filters.LogFilter;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.guice.EndpointsModule;
import org.reflections.Reflections;

import java.util.Set;

public class ServletsModule extends EndpointsModule {

    private static final String ROOT = "/_ah/api/*";

    @Override
    public void configureServlets() {

        super.configureServlets();

        filter(ROOT).through(LogFilter.class);
        filter(ROOT).through(HeaderCORSFilter.class);

        Reflections reflections = new Reflections("com.github");
        Set<Class<?>> endpoints = reflections.getTypesAnnotatedWith(Api.class);
        configureEndpoints(ROOT, endpoints);
    }

}
