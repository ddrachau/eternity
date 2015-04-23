package com.prodyna.pac.eternity.server.rest;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.model.Login;
import com.prodyna.pac.eternity.server.model.RememberMe;
import com.prodyna.pac.eternity.server.model.Session;
import com.prodyna.pac.eternity.server.rest.filter.Authenticated;
import com.prodyna.pac.eternity.server.rest.utils.RestUtils;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.RememberMeService;
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

    @Inject
    private RememberMeService rememberMeService;
    private Response.ResponseBuilder response;

    @GET
    @Authenticated
    public Response ping() {

        return Response.ok().build();

    }

    @POST
    @Consumes(RestUtils.JSON_UTF8)
    @Produces(RestUtils.JSON_UTF8)
    public Response login(@CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie,
                          @Context UriInfo uriInfo, @Context SecurityContext sc, Login login) {

        try {

            if (login.getUsername() == null) {
                throw new InvalidUserException();
            }
            if (login.getPassword() == null) {
                throw new InvalidPasswordException();
            }

            Session session = authenticationService.login(login.getUsername(), login.getPassword());

            Response.ResponseBuilder response = Response.ok();
            response.cookie(createXSRFToken(uriInfo, session.getId()));

            if (login.isRemember()) {

                RememberMe rememberMe = rememberMeService.create(login.getUsername());
                String cookieValue = rememberMe.getId() + ":" + rememberMe.getToken();
                response.cookie(createRememberMeToken(uriInfo, cookieValue));

            } else if (rememberMeCookie != null) {

                response.cookie(expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));

            }

            return response.build();

        } catch (InvalidLoginException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        }

    }

    @DELETE
    @Authenticated
    public Response logout(@Context UriInfo uriInfo, @CookieParam(COOKIE_TOKEN_XSRF) Cookie xsrfCookie,
                           @CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie) {

        authenticationService.logout(xsrfCookie.getValue());

        Response.ResponseBuilder response = Response.ok();

        if (xsrfCookie != null) {
            response.cookie(expireXSRFToken(uriInfo, xsrfCookie.getValue()));
        }
        if (rememberMeCookie != null) {
            response.cookie(expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));
        }

        return response.build();

    }

}
