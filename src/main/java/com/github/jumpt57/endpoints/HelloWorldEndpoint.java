package com.github.jumpt57.endpoints;

import com.github.jumpt57.configuration.auth.Secured;
import com.github.jumpt57.configuration.clientIds.ClientIdsAuthenticator;
import com.github.jumpt57.models.Message;
import com.github.jumpt57.services.HelloWorldService;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(
        name = "core",
        version = "v1",
        clientIds = "*",
        authenticators = ClientIdsAuthenticator.class,
        description = "App Engine Java Starter"
)
public class HelloWorldEndpoint {

    @Inject
    private HelloWorldService helloWorldService;

    @Secured
    @ApiMethod(
            path = "hello/{name}",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public Message helloWorld(User user, @Named("name") String name) {
        return new Message(helloWorldService.hello(name));
    }

}
