package com.prodyna.pac.eternity.server.rest.service.impl;

import com.prodyna.pac.eternity.server.rest.security.Authenticated;
import com.prodyna.pac.eternity.server.rest.service.UserClientService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

import static com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils.COOKIE_TOKEN_XSRF;
import static com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils.JSON_UTF8;

/**
 * Default implementation for the UserClientService.
 */
@Authenticated
@Path("/user")
public class UserClientServiceImpl implements UserClientService {

    @Inject
    private UserService userService;

    @RolesAllowed("USER")
    @GET
    @Produces(JSON_UTF8)
    @Override
    public Response get() {

        return Response.ok(userService.findAll()).build();

    }

    @PermitAll
    @GET
    @Produces(JSON_UTF8)
    @Path("/session")
    @Override
    public Response getBySession(@CookieParam(COOKIE_TOKEN_XSRF) Cookie xsrfCookie) {

        return Response.ok(userService.getBySessionId(xsrfCookie.getValue())).build();

    }

}
