package com.prodyna.pac.eternity.server.rest.service;

import com.prodyna.pac.eternity.server.model.Login;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.prodyna.pac.eternity.server.rest.utils.RestUtils.*;

public interface AuthenticationClientService {

    @GET
    Response ping();

    @POST
    @Consumes(JSON_UTF8)
    @Produces(JSON_UTF8)
    Response login(@CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie,
                   @Context UriInfo uriInfo, @Context SecurityContext sc, Login login);

    @DELETE
    Response logout(@Context UriInfo uriInfo, @CookieParam(COOKIE_TOKEN_XSRF) Cookie xsrfCookie,
                    @CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie);

}
