package com.prodyna.pac.eternity.client.rest.service.authentication;

import com.prodyna.pac.eternity.client.rest.utils.RestCookieUtils;
import com.prodyna.pac.eternity.server.model.authentication.Login;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.CookieParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Client service for authentication.
 */
public interface AuthenticationClientService {

    /**
     * Simple test method to check if the client is authenticated.
     *
     * @return status 200 if authentication was successful
     */
    Response ping();

    /**
     * Login to the system using a Login object with a username and password.
     *
     * @param sessionCookie    optional old session
     * @param rememberMeCookie optional cookie, if set and the new connection is not remembered the cookie should
     *                         expire
     * @param uriInfo          the location of this service
     * @param login            the login container with username and password and the remember flag
     * @return a session cookie and optionally a remember me cookie
     */
    Response login(Cookie sessionCookie, Cookie rememberMeCookie, UriInfo uriInfo, @NotNull @Valid Login login);

    /**
     * Login to the system using a rememberMe object.
     *
     * @param sessionCookie    optional old session
     * @param rememberMeCookie the token to login with
     * @param uriInfo          the location of this service
     * @return a session cookie and optionally a remember me cookie
     */
    Response loginWithToken(@CookieParam(RestCookieUtils.COOKIE_TOKEN_XSRF) Cookie sessionCookie,
                            @CookieParam(RestCookieUtils.COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie,
                            @Context UriInfo uriInfo);

    /**
     * Logs the current user (identified by the xsrf token) out of the system.
     *
     * @param uriInfo          the location of this service
     * @param xsrfCookie       the session
     * @param rememberMeCookie optional cookie, if set and the new connection is not remembered the cookie should
     *                         expire
     * @return 200 if no problem occurred
     */
    Response logout(@Context UriInfo uriInfo, @CookieParam(RestCookieUtils.COOKIE_TOKEN_XSRF) Cookie xsrfCookie,
                    @CookieParam(RestCookieUtils.COOKIE_TOKEN_REMEMBER_ME) Cookie rememberMeCookie);

}
