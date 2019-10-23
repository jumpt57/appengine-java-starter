package com.github.jumpt57.endpoints;

import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.tools.development.testing.DevAppServerTest;
import com.google.appengine.tools.development.testing.DevAppServerTestRunner;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.nio.charset.StandardCharsets;

@RunWith(DevAppServerTestRunner.class)
@DevAppServerTest(EndpointConfig.class)
public class HelloWorldEndpointTest {

    private LocalServiceTestHelper helper = new LocalServiceTestHelper();
    private String port;

    @Before
    public void setUpServer() {
        helper.setUp();
        System.setProperty("environment", "local");
        port = System.getProperty("appengine.devappserver.test.port");
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testHelloWorld() throws Exception {
        // GIVEN
        URL url = new URL("http://localhost:" + port + "/_ah/api/core/v1/hello/Julien");
        // GIVEN
        HTTPResponse response = URLFetchServiceFactory.getURLFetchService()
                .fetch(url);
        String json = new String(response.getContent(), StandardCharsets.UTF_8);
        // THEN
        Assertions.assertThat((String) JsonPath.read(json, "$.message")).isEqualTo("Hello Julien!");
    }

}