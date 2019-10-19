package com.github.jumpt57.listeners;

import com.github.jumpt57.configuration.modules.InterceptorsModule;
import com.github.jumpt57.configuration.modules.ServletsModule;
import com.github.jumpt57.configuration.modules.google.GoogleModuleFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import lombok.extern.slf4j.Slf4j;
import ru.vyarus.guice.validator.ImplicitValidationModule;

import javax.servlet.annotation.WebListener;

@Slf4j
@WebListener
public class GuiceListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new ServletsModule(),
                new InterceptorsModule(),
                new ImplicitValidationModule(),
                GoogleModuleFactory.get()
        );
    }

}
