package com.github.jumpt57.configuration.auth;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.response.UnauthorizedException;
import com.github.jumpt57.configuration.environment.ConfigurationFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;

@Slf4j
public class ValidateUserDomain implements MethodInterceptor {

    private static final String ENVIRONMENT = "environment";

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        if("disable".equals(System.getProperty("endpointAuth"))){
            return methodInvocation.proceed();
        }

        User user = Arrays.stream(methodInvocation.getArguments()).filter(o -> o instanceof User)
                .map(o -> (User) o)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (validateUser(user)) {
            return methodInvocation.proceed();
        } else {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    private boolean validateUser(User user) {
        return user != null && validateDomain(user.getEmail());
    }

    private boolean validateDomain(@NonNull String emailAddress) {
        return ConfigurationFactory.get(System.getProperty(ENVIRONMENT))
                .getAuthDomains().stream()
                .anyMatch(emailAddress::contains);
    }

}
