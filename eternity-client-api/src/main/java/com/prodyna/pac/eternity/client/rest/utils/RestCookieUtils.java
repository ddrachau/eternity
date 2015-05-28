package com.prodyna.pac.eternity.client.rest.utils;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;

/**
 * Helper class with tools for creating special cookies.
 */
public final class RestCookieUtils {

    /**
     * Media type for the response format
     */
    public static final String JSON_UTF8 = MediaType.APPLICATION_JSON + "; charset=UTF-8";

    /**
     * name for the session header
     */
    public static final String HEADER_TOKEN_XSRF = "X-XSRF-TOKEN";

    /**
     * Cookie name for the session token
     */
    public static final String COOKIE_TOKEN_XSRF = "XSRF-TOKEN";

    /**
     * Cookie name for the remember me token
     */
    public static final String COOKIE_TOKEN_REMEMBER_ME = "REMEMBER-ME";

    /**
     * time how long a remember me token should be stored on the client (14 days)
     */
    private static final int TTL_TOKEN_REMEMBER_ME = 60 * 60 * 24 * 14;

    /**
     * time to live for the session token
     */
    private static final int TTL_TOKEN_XSRF = -1;

    /**
     * expire constant
     */
    private static final int TTL_TOKEN_EXPIRE = 0;

    /**
     * Private constructor to avoid instantiation
     */
    private RestCookieUtils() {

    }

    /**
     * creates a new XSRF token
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    public static NewCookie createXSRFToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_XSRF, uriInfo, cookieValue, TTL_TOKEN_XSRF);
    }

    /**
     * creates an expiry token for the given session value
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    public static NewCookie expireXSRFToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_XSRF, uriInfo, cookieValue, TTL_TOKEN_EXPIRE);
    }

    /**
     * creates a new remember me token
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    public static NewCookie createRememberMeToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_REMEMBER_ME, uriInfo, cookieValue, TTL_TOKEN_REMEMBER_ME);
    }

    /**
     * creates an expiry token for the given remember me
     *
     * @param uriInfo     the uri
     * @param cookieValue the value
     * @return the new token
     */
    public static NewCookie expireRememberMeToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_REMEMBER_ME, uriInfo, cookieValue, TTL_TOKEN_EXPIRE);
    }

    /**
     * Helper method for creating cookie token
     *
     * @param cookieId    the id
     * @param uriInfo     the uri info for which the cookie should be created
     * @param cookieValue the value for the cookie
     * @param maxAge      the max age
     * @return the created cookie
     */
    private static NewCookie createToken(@NotNull final String cookieId, @NotNull final UriInfo uriInfo,
                                         @NotNull final String cookieValue, final int maxAge) {

        String restPath = uriInfo.getBaseUri().getPath();
        String applicationPath = restPath.substring(0, restPath.indexOf("/", 1) + 1);
        String cookiePath = applicationPath;
        boolean cookieOnlySecure = false;
        NewCookie cookie = new NewCookie(cookieId, cookieValue, cookiePath, null, null, maxAge, cookieOnlySecure);

        return cookie;

    }

}
