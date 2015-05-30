package com.prodyna.pac.eternity.common.helper;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;

/**
 * Helper class with tools for creating special cookies.
 */
public interface RestCookieBuilder {

    /**
     * Media type for the response format
     */
    String JSON_UTF8 = MediaType.APPLICATION_JSON + "; charset=UTF-8";

    /**
     * name for the session header
     */
    String HEADER_TOKEN_XSRF = "X-XSRF-TOKEN";

    /**
     * Cookie name for the session token
     */
    String COOKIE_TOKEN_XSRF = "XSRF-TOKEN";

    /**
     * Cookie name for the remember me token
     */
    String COOKIE_TOKEN_REMEMBER_ME = "REMEMBER-ME";

    /**
     * creates a new XSRF token
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    NewCookie createXSRFToken(@NotNull UriInfo uriInfo, @NotNull String cookieValue);

    /**
     * creates an expiry token for the given session value
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    NewCookie expireXSRFToken(@NotNull UriInfo uriInfo, @NotNull String cookieValue);

    /**
     * creates a new remember me token
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    NewCookie createRememberMeToken(@NotNull UriInfo uriInfo, @NotNull String cookieValue);

    /**
     * creates an expiry token for the given remember me
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    NewCookie expireRememberMeToken(@NotNull UriInfo uriInfo, @NotNull String cookieValue);

}
