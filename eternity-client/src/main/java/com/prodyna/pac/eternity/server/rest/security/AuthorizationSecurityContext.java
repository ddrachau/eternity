package com.prodyna.pac.eternity.server.rest.security;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AuthorizationSecurityContext implements SecurityContext {

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {

        System.out.println("I am asked for role:" + s);
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }

}
