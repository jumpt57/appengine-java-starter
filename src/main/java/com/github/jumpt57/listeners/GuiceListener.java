package com.github.jumpt57.listeners;

import com.github.jumpt57.configuration.modules.InterceptorsModule;
import com.github.jumpt57.configuration.modules.ServletsModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import ru.vyarus.guice.validator.ImplicitValidationModule;

public class GuiceListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new ServletsModule(),
                new InterceptorsModule(),
                new ImplicitValidationModule()
        );
    }

}
