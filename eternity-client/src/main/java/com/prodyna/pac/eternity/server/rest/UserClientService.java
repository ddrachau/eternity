package com.prodyna.pac.eternity.server.rest;

import com.prodyna.pac.eternity.server.rest.utils.RestUtils;
import com.prodyna.pac.eternity.server.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserClientService {

    @Inject
    private UserService userService;

    @GET
    @Produces(RestUtils.JSON_UTF8)
    public Response get() {
        return Response.ok(userService.findAll()).build();
    }

}
