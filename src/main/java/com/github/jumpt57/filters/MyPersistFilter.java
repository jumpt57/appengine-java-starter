package com.github.jumpt57.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
@Singleton
public class MyPersistFilter implements Filter {

    private final UnitOfWork unitOfWork;
    private final PersistService persistService;

    @Inject
    public MyPersistFilter(UnitOfWork unitOfWork, PersistService persistService) {
        this.unitOfWork = unitOfWork;
        this.persistService = persistService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        persistService.start();
        try {
            unitOfWork.begin();
        } catch (Exception e) {
            log.warn("It was not possible to start the unitOfWork because it was already stared");
        }
    }

    @Override
    public void destroy() {
        persistService.stop();
        try {
            unitOfWork.end();
        } catch (Exception e) {
            log.error("It was not possible to end the unitOfWork", e);
        }
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
