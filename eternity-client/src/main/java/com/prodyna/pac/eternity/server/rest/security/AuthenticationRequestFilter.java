package com.prodyna.pac.eternity.server.rest.security;

import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.service.SessionService;
import com.prodyna.pac.eternity.server.service.UserService;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

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

    @Inject
    private UserService userService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String xsrfToken = containerRequestContext.getHeaderString(RestCookieUtils.HEADER_TOKEN_XSRF);

        Session session = sessionService.get(xsrfToken);

        if (session == null) {
            containerRequestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"No valid session\"}").build());
            return;
        }

        User user = userService.getBySessionId(session.getId());

        containerRequestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
                return () -> user.getIdentifier();
            }

            @Override
            public boolean isUserInRole(String s) {
                return user.getRole().name().equals(s);
            }

            @Override
            public boolean isSecure() {
                return containerRequestContext.getSecurityContext().isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return containerRequestContext.getSecurityContext().getAuthenticationScheme();
            }

        });

    }

}
