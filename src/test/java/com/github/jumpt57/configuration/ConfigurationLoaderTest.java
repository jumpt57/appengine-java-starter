package com.github.jumpt57.configuration;

import com.github.jumpt57.BaseTest;
import com.github.jumpt57.configuration.environment.ConfigurationFactory;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.List;

public class ConfigurationLoaderTest extends BaseTest {

    @Test
    public void should_get_configuration_for_application() {
        // GIVEN
        // WHEN
        List<String> authDomains = ConfigurationFactory.get()
                .getAuthDomains();
        // THEN
        Assertions.assertThat(authDomains)
                .isNotEmpty();
    }

}