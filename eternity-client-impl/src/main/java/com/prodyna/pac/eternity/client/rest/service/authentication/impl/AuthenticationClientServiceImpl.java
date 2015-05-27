package com.prodyna.pac.eternity.client.rest.service.authentication.impl;

import com.prodyna.pac.eternity.client.rest.security.Authenticated;
import com.prodyna.pac.eternity.client.rest.service.authentication.AuthenticationClientService;
import com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.components.common.RememberMeUtils;
import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidTokenException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.model.authentication.Login;
import com.prodyna.pac.eternity.server.service.authentication.AuthenticationService;
import com.prodyna.pac.eternity.server.service.authentication.SessionService;
import org.slf4j.Logger;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.COOKIE_TOKEN_REMEMBER_ME;
import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.COOKIE_TOKEN_XSRF;
import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.JSON_UTF8;
import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.createRememberMeToken;
import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.createXSRFToken;
import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.expireRememberMeToken;
import static com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils.expireXSRFToken;

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
    @Produces(RestCookieUtils.JSON_UTF8)
    @Override
    public Response login(@CookieParam(COOKIE_TOKEN_XSRF) final Cookie sessionCookie,
                          @CookieParam(COOKIE_TOKEN_REMEMBER_ME) final Cookie rememberMeCookie,
                          @Context final UriInfo uriInfo, @NotNull @Valid final Login login) {

        try {

            if (login.getUsername() == null) {
                throw new InvalidUserException();
            }
            if (login.getPassword() == null) {
                throw new InvalidPasswordException();
            }

            Login performedLogin = authenticationService.login(login);

            if (sessionCookie != null) {
                sessionService.delete(sessionCookie.getValue());
            }

            Response.ResponseBuilder response = Response.ok().entity(performedLogin.getUser());
            response.cookie(createXSRFToken(uriInfo, performedLogin.getXsrfToken()));

            if (performedLogin.isRemember()) {

                response.cookie(createRememberMeToken(uriInfo, performedLogin.getRememberMeToken()));

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
    public Response loginWithToken(@CookieParam(COOKIE_TOKEN_XSRF) final Cookie sessionCookie,
                                   @CookieParam(COOKIE_TOKEN_REMEMBER_ME) final Cookie rememberMeCookie,
                                   @Context final UriInfo uriInfo) {

        try {

            if (rememberMeCookie == null || !RememberMeUtils.isValidToken(rememberMeCookie.getValue())) {
                throw new InvalidTokenException();
            }

            Login login = authenticationService.login(rememberMeCookie.getValue());

            if (sessionCookie != null) {
                sessionService.delete(sessionCookie.getValue());
            }

            Response.ResponseBuilder response = Response.ok(login.getUser());
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
    public Response logout(@Context final UriInfo uriInfo, @CookieParam(COOKIE_TOKEN_XSRF) final Cookie xsrfCookie,
                           @CookieParam(COOKIE_TOKEN_REMEMBER_ME) final Cookie rememberMeCookie) {

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
