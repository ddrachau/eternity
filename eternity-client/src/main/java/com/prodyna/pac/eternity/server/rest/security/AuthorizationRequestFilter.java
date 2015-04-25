package com.prodyna.pac.eternity.server.rest.security;

import com.prodyna.pac.eternity.server.service.SessionService;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.slf4j.Logger;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication filter implementation for rest services.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Inject
    private SessionService sessionService;

    /**
     * Default response for forbidden access.
     */
    private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker)
                containerRequestContext.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        Method method = methodInvoker.getMethod();

        if (method.isAnnotationPresent(DenyAll.class)) {

            containerRequestContext.abortWith(ACCESS_FORBIDDEN);
            return;

        } else if (method.isAnnotationPresent(RolesAllowed.class)) {

            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            List<String> rolesSet = new ArrayList<>(Arrays.asList(rolesAnnotation.value()));

            for (String role : rolesSet) {

                if (containerRequestContext.getSecurityContext().isUserInRole(role)) {
                    return;
                }

            }

            containerRequestContext.abortWith(ACCESS_FORBIDDEN);
            return;

        }

    }

}
