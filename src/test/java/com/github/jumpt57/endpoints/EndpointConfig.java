package com.github.jumpt57.endpoints;

import com.google.appengine.tools.development.testing.BaseDevAppServerTestConfig;
import com.google.common.io.ByteStreams;
import lombok.NonNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EndpointConfig extends BaseDevAppServerTestConfig {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    private static final String SDK_COMMAND_WINDOWS = "gcloud.cmd info --format=\"value(installation.sdk_root)\"";
    private static final String SDK_COMMAND_LINUX = "gcloud info --format=\"value(installation.sdk_root)\"";

    private static final String SDK_JAVA_PATH_WINDOWS = "\\platform\\google_appengine\\google\\appengine\\tools\\java";
    private static final String SDK_JAVA_PATH_LINUX = "/platform/google_appengine/google/appengine/tools/java";

    private static final String WEB_APP_PATH = "\\src\\main\\webapp";

    @Override
    public File getSdkRoot() {
        try {
            Process runtime = Runtime.getRuntime().exec(getSDKCommand());
            runtime.waitFor();

            String baseSdkPath = new String(ByteStreams.toByteArray(runtime.getInputStream()));
            baseSdkPath = baseSdkPath.replace(System.lineSeparator(), "");

            if (baseSdkPath.isEmpty()) {
                throw new IllegalStateException("Gcloud was not found ! Try installing GCloud.");
            }

            File file = new File(getPath(baseSdkPath));

            if (!file.exists()) {
                throw new IllegalStateException("AppEngine Java SDK not found ! Try : gcloud components install app-engine-java");
            }

            return file;
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

    private String[] getSDKCommand() {
        if (IS_WINDOWS) {
            return new String[]{
                    "cmd.exe", "/C", SDK_COMMAND_WINDOWS
            };
        } else {
            return new String[]{
                    "/bin/bash", "-c", "-l", SDK_COMMAND_LINUX
            };
        }
    }

    private String getPath(@NonNull String basePath) {
        if (IS_WINDOWS) {
            return basePath + SDK_JAVA_PATH_WINDOWS;
        } else {
            return basePath + SDK_JAVA_PATH_LINUX;
        }
    }

}
