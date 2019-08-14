package com.github.jumpt57.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class EnvironmentServiceTest {

    private EnvironmentService environmentService;

    @BeforeClass
    public void setUp() {
        System.setProperty("environment", "local");
        final Injector injector = Guice.createInjector();
        environmentService = injector.getInstance(EnvironmentService.class);
    }

    @Test
    public void should_call_service() {
        // GIVEN
        String name = "User";
        // WHEN
        final String hello = environmentService.get();
        // THEN
        Assertions.assertThat(hello)
                .isEqualTo("local");
    }

}