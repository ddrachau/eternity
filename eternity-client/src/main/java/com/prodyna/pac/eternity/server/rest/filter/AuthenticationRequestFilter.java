package com.prodyna.pac.eternity.server.rest.filter;

import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.rest.utils.RestUtils;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
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

@Provider
@Authenticated
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationRequestFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Inject
    private AuthenticationService authenticationService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String xsrfToken = containerRequestContext.getHeaderString(RestUtils.XSRF_HEADER_TOKEN);
        Cookie xsrfCookie = containerRequestContext.getCookies().get(RestUtils.XSRF_COOKIE_TOKEN);

        logger.info("token: " + xsrfToken);
        logger.info("cookie: " + xsrfCookie);

        Session session = authenticationService.getSession(xsrfToken);

        if (session == null) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"No valid session\"}").build());
        }

    }

}
