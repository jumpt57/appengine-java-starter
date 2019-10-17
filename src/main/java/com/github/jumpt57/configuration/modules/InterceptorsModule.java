package com.github.jumpt57.configuration.modules;

import com.github.jumpt57.configuration.auth.Cron;
import com.github.jumpt57.configuration.auth.Secured;
import com.github.jumpt57.configuration.auth.ValidateCron;
import com.github.jumpt57.configuration.auth.ValidateUserDomain;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class InterceptorsModule extends AbstractModule {

    @Override
    protected void configure() {

        ValidateUserDomain validateUserDomain = new ValidateUserDomain();
        requestInjection(validateUserDomain);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Secured.class),
                validateUserDomain);

        ValidateCron validateCron = new ValidateCron();
        requestInjection(validateCron);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Cron.class),
                validateCron);

    }

}
