package com.prodyna.pac.eternity.common.helper.impl;

import com.prodyna.pac.eternity.common.helper.RestCookieBuilder;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;

/**
 * Default implementation for a RestCookieBuilder
 */
public class RestCookieBuilderImpl implements RestCookieBuilder {

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

    @Override
    public NewCookie createXSRFToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return this.createToken(RestCookieBuilder.COOKIE_TOKEN_XSRF, uriInfo, cookieValue, TTL_TOKEN_XSRF);

    }

    @Override
    public NewCookie expireXSRFToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return this.createToken(RestCookieBuilder.COOKIE_TOKEN_XSRF, uriInfo, cookieValue, TTL_TOKEN_EXPIRE);

    }

    @Override
    public NewCookie createRememberMeToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return this.createToken(RestCookieBuilder.COOKIE_TOKEN_REMEMBER_ME, uriInfo, cookieValue,
                TTL_TOKEN_REMEMBER_ME);

    }

    @Override
    public NewCookie expireRememberMeToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return this.createToken(RestCookieBuilder.COOKIE_TOKEN_REMEMBER_ME, uriInfo, cookieValue, TTL_TOKEN_EXPIRE);

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
    private NewCookie createToken(@NotNull final String cookieId, @NotNull final UriInfo uriInfo,
                                  @NotNull final String cookieValue, final int maxAge) {

        String restPath = uriInfo.getBaseUri().getPath();
        String applicationPath = restPath.substring(0, restPath.indexOf("/", 1) + 1);
        String cookiePath = applicationPath;
        boolean cookieOnlySecure = false;
        NewCookie cookie = new NewCookie(cookieId, cookieValue, cookiePath, null, null, maxAge, cookieOnlySecure);

        return cookie;

    }

}
