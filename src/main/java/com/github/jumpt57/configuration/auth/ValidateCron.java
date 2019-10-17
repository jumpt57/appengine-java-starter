package com.github.jumpt57.configuration.auth;

import com.google.api.server.spi.response.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
public class ValidateCron implements MethodInterceptor {

    public static final String CRON_HEADER = "X-Appengine-Cron";

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        if (System.getProperty("environment").equals("local")) {
            return methodInvocation.proceed();
        }

        HttpServletRequest request = Arrays.stream(methodInvocation.getArguments()).filter(o -> o instanceof HttpServletRequest)
                .map(o -> (HttpServletRequest) o)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (request.getHeader(CRON_HEADER) != null) {
            return methodInvocation.proceed();
        } else {
            throw new UnauthorizedException("Action should be performed by cron");
        }
    }

}
