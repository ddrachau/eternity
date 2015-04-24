package com.prodyna.pac.eternity.server.rest.service.impl;

import com.prodyna.pac.eternity.server.rest.filter.Authenticated;
import com.prodyna.pac.eternity.server.rest.service.UserClientService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Default implementation for the UserClientService.
 */
@Authenticated
@Path("/user")
public class UserClientServiceImpl implements UserClientService {

    @Inject
    private UserService userService;

    @Override
    public Response get() {

        return Response.ok(userService.findAll()).build();

    }

}
