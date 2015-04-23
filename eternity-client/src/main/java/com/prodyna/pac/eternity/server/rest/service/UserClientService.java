package com.prodyna.pac.eternity.server.rest.service;

import com.prodyna.pac.eternity.server.rest.utils.RestUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public interface UserClientService {

    @GET
    @Produces(RestUtils.JSON_UTF8)
    Response get();

}
