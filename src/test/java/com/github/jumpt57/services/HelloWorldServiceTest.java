package com.github.jumpt57.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HelloWorldServiceTest {

    private HelloWorldService helloWorldService;

    @BeforeClass
    public void setUp() {
        final Injector injector = Guice.createInjector();
        helloWorldService = injector.getInstance(HelloWorldService.class);
    }

    @Test
    public void should_call_service(){
        // GIVEN
        String name = "User";
        // WHEN
        final String hello = helloWorldService.hello(name);
        // THEN
        Assertions.assertThat(hello)
                .isEqualTo("Hello User!");
    }

}