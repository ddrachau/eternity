package com.prodyna.pac.eternity.server.rest.impl;

import com.prodyna.pac.eternity.server.rest.UserClientService;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserClientServiceImpl implements UserClientService {

    @Inject
    private UserService userService;

    @Override
    public Response get() {

        return Response.ok(userService.findAll()).build();

    }

}
