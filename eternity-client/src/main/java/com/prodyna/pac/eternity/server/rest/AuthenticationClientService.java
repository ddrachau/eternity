package com.prodyna.pac.eternity.server.rest;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.model.Login;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.rest.filter.Authenticated;
import com.prodyna.pac.eternity.server.rest.utils.RestUtils;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import static com.prodyna.pac.eternity.server.rest.utils.RestUtils.*;


@Path("/auth")
public class AuthenticationClientService {

    @Inject
    private Logger logger;

    @Inject
    private AuthenticationService authenticationService;

    @GET
    @Authenticated
    public Response ping() {

        return Response.ok().build();

    }

    @POST
    @Consumes(RestUtils.JSON_UTF8)
    @Produces(RestUtils.JSON_UTF8)
    public Response login(@Context UriInfo uriInfo, @Context SecurityContext sc, Login login) {

        try {

            Session session = authenticationService.login(login.getUsername(), login.getPassword());

            NewCookie cookie = createXSRFToken(uriInfo, session.getId());
            return Response.ok().cookie(cookie).build();

        } catch (InvalidLoginException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        }

    }

    @DELETE
    @Authenticated
    public Response logout(@Context UriInfo uriInfo, @CookieParam(XSRF_COOKIE_TOKEN) Cookie cookie) {

        authenticationService.logout(cookie.getValue());

        NewCookie session = expireToken(uriInfo, cookie);

        return Response.status(200).cookie(session).build();

    }

}
