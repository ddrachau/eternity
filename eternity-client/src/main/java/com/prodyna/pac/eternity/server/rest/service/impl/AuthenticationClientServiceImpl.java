package com.prodyna.pac.eternity.server.rest.service.impl;

import com.prodyna.pac.eternity.server.exception.functional.InvalidLoginException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidUserException;
import com.prodyna.pac.eternity.server.model.Login;
import com.prodyna.pac.eternity.server.rest.filter.Authenticated;
import com.prodyna.pac.eternity.server.rest.service.AuthenticationClientService;
import com.prodyna.pac.eternity.server.service.AuthenticationService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import static com.prodyna.pac.eternity.server.rest.utils.RestUtils.*;

/**
 * Default implementation for the AuthenticationClientService.
 */
@Path("/auth")
public class AuthenticationClientServiceImpl implements AuthenticationClientService {

    @Inject
    private Logger logger;

    @Inject
    private AuthenticationService authenticationService;

    @Authenticated
    @Override
    public Response ping() {

        return Response.ok().build();

    }

    @Override
    public Response login(Cookie rememberMeCookie, UriInfo uriInfo, SecurityContext sc, Login login) {

        try {

            if (login.getUsername() == null) {
                throw new InvalidUserException();
            }
            if (login.getPassword() == null) {
                throw new InvalidPasswordException();
            }

            login = authenticationService.login(login);

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

    @Authenticated
    @Override
    public Response logout(UriInfo uriInfo, Cookie xsrfCookie, Cookie rememberMeCookie) {

        authenticationService.logout(xsrfCookie.getValue());

        Response.ResponseBuilder response = Response.ok();

        response.cookie(expireXSRFToken(uriInfo, xsrfCookie.getValue()));

        if (rememberMeCookie != null) {
            response.cookie(expireRememberMeToken(uriInfo, rememberMeCookie.getValue()));
        }

        return response.build();

    }

}
