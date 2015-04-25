package com.prodyna.pac.eternity.server.rest.service.impl;

import com.prodyna.pac.eternity.server.common.RememberMeUtils;
import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidTokenException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.model.Login;
import com.prodyna.pac.eternity.server.rest.security.Authenticated;
import com.prodyna.pac.eternity.server.rest.service.AuthenticationClientService;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import com.prodyna.pac.eternity.server.service.SessionService;
import org.slf4j.Logger;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static com.prodyna.pac.eternity.server.rest.utils.RestCookieUtils.*;

/**
 * Default implementation for the AuthenticationClientService.
 */
@PermitAll
@Path("/auth")
public class AuthenticationClientServiceImpl implements AuthenticationClientService {

    @Inject
    private Logger logger;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private SessionService sessionService;

    @Authenticated
    @GET
    @Override
    public Response ping() {

        return Response.ok().build();

    }

    @POST
    @Consumes(JSON_UTF8)
    @Produces(JSON_UTF8)
    @Override
    public Response login(@CookieParam(COOKIE_TOKEN_XSRF) Cookie sessionCookie,
                          @CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie,
                          @Context UriInfo uriInfo, Login login) {

        try {

            if (login.getUsername() == null) {
                throw new InvalidUserException();
            }
            if (login.getPassword() == null) {
                throw new InvalidPasswordException();
            }

            login = authenticationService.login(login);

            if (sessionCookie != null) {
                sessionService.delete(sessionCookie.getValue());
            }

            Response.ResponseBuilder response = Response.ok();
            response.cookie(createXSRFToken(uriInfo, login.getXsrfToken()));

            if (login.isRemember()) {

                response.cookie(createRememberMeToken(uriInfo, login.getRememberMeToken()));

            } else if (rememberMeCookie != null) {

                response.cookie(expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));

            }

            return response.build();

        } catch (InvalidLoginException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        }

    }

    @GET
    @Produces(JSON_UTF8)
    @Path("/token")
    @Override
    public Response loginWithToken(@CookieParam(COOKIE_TOKEN_XSRF) Cookie sessionCookie,
                                   @CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie,
                                   @Context UriInfo uriInfo) {

        try {

            if (rememberMeCookie == null || !RememberMeUtils.isValidToken(rememberMeCookie.getValue())) {
                throw new InvalidTokenException();
            }

            Login login = authenticationService.login(rememberMeCookie.getValue());

            if (sessionCookie != null) {
                sessionService.delete(sessionCookie.getValue());
            }

            Response.ResponseBuilder response = Response.ok();
            response.cookie(createXSRFToken(uriInfo, login.getXsrfToken()));
            response.cookie(createRememberMeToken(uriInfo, login.getRememberMeToken()));

            return response.build();

        } catch (InvalidLoginException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        }
    }

    @Authenticated
    @DELETE
    @Override
    public Response logout(@Context UriInfo uriInfo, @CookieParam(COOKIE_TOKEN_XSRF) Cookie xsrfCookie,
                           @CookieParam(COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie) {

        String rememberMeToken = rememberMeCookie != null ? rememberMeCookie.getValue() : null;
        authenticationService.logout(xsrfCookie.getValue(), rememberMeToken);

        Response.ResponseBuilder response = Response.ok();

        response.cookie(expireXSRFToken(uriInfo, xsrfCookie.getValue()));

        if (rememberMeCookie != null) {
            response.cookie(expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));
        }

        return response.build();

    }

}
