package com.github.jumpt57;

import com.github.jumpt57.configuration.modules.InterceptorsModule;
import com.github.jumpt57.configuration.modules.ServletsModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.BeforeClass;
import ru.vyarus.guice.validator.ImplicitValidationModule;

public class BaseTest {

    private static final String ENVIRONMENT = "environment";

    protected Injector injector;

    @BeforeClass
    public void setUp() {

        if (System.getProperty(ENVIRONMENT) == null) {
            System.setProperty(ENVIRONMENT, "local");
        }

        injector = Guice.createInjector(
                new ServletsModule(),
                new ImplicitValidationModule(),
                new InterceptorsModule()
        );
    }

}
