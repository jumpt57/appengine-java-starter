package com.github.jumpt57.listeners;

import com.google.appengine.api.LifecycleManager;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        LifecycleManager.getInstance().setShutdownHook(() -> log.info("Shutting down..."));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
