package com.github.jumpt57.services;


import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
public class HelloWorldService {

    public String hello(@NotNull String name) {
        return String.format("Hello %s!", name);
    }

}
