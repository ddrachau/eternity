package com.prodyna.pac.eternity.authentication.client.impl;

import com.prodyna.pac.eternity.authentication.client.AuthenticationClientService;
import com.prodyna.pac.eternity.authentication.service.AuthenticationService;
import com.prodyna.pac.eternity.authentication.service.SessionService;
import com.prodyna.pac.eternity.common.client.Authenticated;
import com.prodyna.pac.eternity.common.helper.RememberMeAccessor;
import com.prodyna.pac.eternity.common.helper.RestCookieBuilder;
import com.prodyna.pac.eternity.common.logging.Logging;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidTokenException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.common.model.authentication.Login;
import com.prodyna.pac.eternity.common.profiling.Profiling;
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

/**
 * Default implementation for the AuthenticationClientService.
 */
@Logging
@Profiling
@PermitAll
@Path("/auth")
public class AuthenticationClientServiceImpl implements AuthenticationClientService {

    @Inject
    private Logger logger;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private SessionService sessionService;

    @Inject
    private RememberMeAccessor rememberMeAccessor;

    @Inject
    private RestCookieBuilder restCookieBuilder;

    @Authenticated
    @GET
    @Override
    public Response ping() {

        return Response.ok().build();

    }

    @POST
    @Consumes(RestCookieBuilder.JSON_UTF8)
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Override
    public Response login(@CookieParam(RestCookieBuilder.COOKIE_TOKEN_XSRF) final Cookie sessionCookie,
                          @CookieParam(RestCookieBuilder.COOKIE_TOKEN_REMEMBER_ME) final Cookie rememberMeCookie,
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
            response.cookie(restCookieBuilder.createXSRFToken(uriInfo, performedLogin.getXsrfToken()));

            if (performedLogin.isRemember()) {

                response.cookie(restCookieBuilder.createRememberMeToken(uriInfo, performedLogin.getRememberMeToken()));

            } else if (rememberMeCookie != null) {

                response.cookie(restCookieBuilder.expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));

            }

            return response.build();

        } catch (InvalidLoginException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        }

    }

    @GET
    @Produces(RestCookieBuilder.JSON_UTF8)
    @Path("/token")
    @Override
    public Response loginWithToken(@CookieParam(RestCookieBuilder.COOKIE_TOKEN_XSRF) final Cookie sessionCookie,
                                   @CookieParam(RestCookieBuilder.COOKIE_TOKEN_REMEMBER_ME)
                                   final Cookie rememberMeCookie, @Context final UriInfo uriInfo) {

        try {

            if (rememberMeCookie == null || !rememberMeAccessor.isValidToken(rememberMeCookie.getValue())) {
                throw new InvalidTokenException();
            }

            Login login = authenticationService.login(rememberMeCookie.getValue());

            if (sessionCookie != null) {
                sessionService.delete(sessionCookie.getValue());
            }

            Response.ResponseBuilder response = Response.ok(login.getUser());
            response.cookie(restCookieBuilder.createXSRFToken(uriInfo, login.getXsrfToken()));
            response.cookie(restCookieBuilder.createRememberMeToken(uriInfo, login.getRememberMeToken()));

            return response.build();

        } catch (InvalidLoginException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();

        }
    }

    @Authenticated
    @DELETE
    @Override
    public Response logout(@Context final UriInfo uriInfo,
                           @CookieParam(RestCookieBuilder.COOKIE_TOKEN_XSRF) final Cookie xsrfCookie,
                           @CookieParam(RestCookieBuilder.COOKIE_TOKEN_REMEMBER_ME) final Cookie rememberMeCookie) {

        String rememberMeToken = rememberMeCookie != null ? rememberMeCookie.getValue() : null;
        authenticationService.logout(xsrfCookie.getValue(), rememberMeToken);

        Response.ResponseBuilder response = Response.ok();

        response.cookie(restCookieBuilder.expireXSRFToken(uriInfo, xsrfCookie.getValue()));

        if (rememberMeCookie != null) {
            response.cookie(restCookieBuilder.expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));
        }

        return response.build();

    }

}
