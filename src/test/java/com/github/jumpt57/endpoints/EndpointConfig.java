package com.github.jumpt57.endpoints;

import com.google.appengine.tools.development.testing.BaseDevAppServerTestConfig;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EndpointConfig extends BaseDevAppServerTestConfig {

    private static final String SDK_COMMAND = "gcloud.cmd info --format=\"value(installation.sdk_root)\"";
    private static final String WEB_APP_PATH = "\\src\\main\\webapp";
    private static final String SDK_JAVA_PATH = "\\platform\\google_appengine\\google\\appengine\\tools\\java";

    @Override
    public File getSdkRoot() {
        try {
            Process runtime = Runtime.getRuntime().exec(SDK_COMMAND);
            String baseSdkPath = new String(ByteStreams.toByteArray(runtime.getInputStream()));
            baseSdkPath = baseSdkPath.replace(System.lineSeparator(), "");
            return new File(baseSdkPath + SDK_JAVA_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getAppDir() {
        return new File(System.getProperty("user.dir") + WEB_APP_PATH);
    }

    @Override
    public List<URL> getClasspath() {
        List<URL> classPath = new ArrayList<>();
        try {
            String separator = System.getProperty("path.separator");
            String[] pathElements = System.getProperty("java.class.path").split(separator);
            for (String pathElement : pathElements) {
                classPath.add(new File(pathElement).toURI().toURL());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return classPath;
    }

}
