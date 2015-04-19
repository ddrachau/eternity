package com.prodyna.pac.eternity.server.rest;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Map<String, Cookie> cookies = containerRequestContext.getCookies();
        for (Map.Entry<String, Cookie> c : cookies.entrySet()) {
            logger.info(c.getKey() + " - " + c.getValue());
        }
    }

}
