package com.prodyna.pac.eternity.client.rest.security;

import com.prodyna.pac.eternity.server.model.user.User;

import javax.validation.constraints.NotNull;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Implementation for checking rules for the given user.
 */
public class AuthorizationSecurityContext implements SecurityContext {

    /**
     * the user this context represents
     */
    private User user;

    /**
     * the request context
     */
    private ContainerRequestContext context;

    /**
     * Default constructor
     *
     * @param newUser    the user this context represents
     * @param newContext the request context
     */
    public AuthorizationSecurityContext(@NotNull final User newUser,
                                        @NotNull final ContainerRequestContext newContext) {

        this.user = newUser;
        this.context = newContext;

    }

    @Override
    public final Principal getUserPrincipal() {

        return () -> user.getIdentifier();

    }

    @Override
    public final boolean isUserInRole(final String s) {

        return user.getRole().name().equals(s);

    }

    @Override
    public final boolean isSecure() {

        return this.context.getSecurityContext().isSecure();

    }

    @Override
    public final String getAuthenticationScheme() {

        return this.context.getSecurityContext().getAuthenticationScheme();

    }

    /**
     * Basic getter
     *
     * @return the user
     */
    public User getUser() {

        return user;
    }
}
