package com.prodyna.pac.eternity.server.rest.service.impl;

import com.prodyna.pac.eternity.server.rest.security.Authenticated;
import com.prodyna.pac.eternity.server.rest.service.UserClientService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils.JSON_UTF8;

/**
 * Default implementation for the UserClientService.
 */
@Authenticated
@Path("/user")
public class UserClientServiceImpl implements UserClientService {

    @Inject
    private UserService userService;

    @GET
    @Produces(JSON_UTF8)
    @Override
    public Response get() {

        return Response.ok(userService.findAll()).build();

    }

}
