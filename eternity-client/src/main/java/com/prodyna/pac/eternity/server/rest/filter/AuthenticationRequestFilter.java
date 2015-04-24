package com.prodyna.pac.eternity.server.rest.filter;

import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.service.SessionService;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Authentication filter implementation for rest services.
 */
@Provider
@Authenticated
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationRequestFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Inject
    private SessionService sessionService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String xsrfToken = containerRequestContext.getHeaderString(RestCookieUtils.HEADER_TOKEN_XSRF);
        Cookie xsrfCookie = containerRequestContext.getCookies().get(RestCookieUtils.COOKIE_TOKEN_XSRF);

        logger.info("token: " + xsrfToken);
        logger.info("cookie: " + xsrfCookie);

        Session session = sessionService.get(xsrfToken);

        if (session == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"No valid session\"}").build());
        }

    }

}
