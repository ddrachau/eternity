package com.prodyna.pac.eternity.server.rest;

import com.prodyna.pac.eternity.server.logging.Logging;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.rest.utils.RestUtils;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.UserService;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

import static com.prodyna.pac.eternity.server.rest.utils.RestUtils.*;

@Path("/auth")
public class AuthenticationClientService {

    @Inject
    private Logger logger;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private UserService userService;

    @GET
    public Response ping() {
        return Response.ok().build();
    }

    @POST
    @Produces(RestUtils.JSON_UTF8)
    public Response login(@Context UriInfo uriInfo, @Context SecurityContext sc, User user) {

        NewCookie session = createXSRFToken(uriInfo, UUID.randomUUID().toString());

        // TODO verify? create session?
        if (user.getIdentifier().equals("admin")) {

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        logger.info("login:\n" + session);

        return Response.ok().cookie(session).build();

    }

    @DELETE
    public Response logout(@Context UriInfo uriInfo, @CookieParam(XSRF_TOKEN) Cookie cookie) {

        // TODO delete session in DB
        //authenticationService.logout();

        NewCookie session = expireToken(uriInfo, cookie);

        return Response.status(200).cookie(session).build();

    }

}
