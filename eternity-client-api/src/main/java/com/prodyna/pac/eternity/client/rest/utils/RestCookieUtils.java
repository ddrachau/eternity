package com.prodyna.pac.eternity.client.rest.utils;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;

/**
 * Helper class with tools for creating special cookies.
 */
public abstract class RestCookieUtils {

    public static final String JSON_UTF8 = MediaType.APPLICATION_JSON + "; charset=UTF-8";

    public static final String HEADER_TOKEN_XSRF = "X-XSRF-TOKEN";

    public static final String COOKIE_TOKEN_XSRF = "XSRF-TOKEN";

    public static final String COOKIE_TOKEN_REMEMBER_ME = "REMEMBER-ME";

    private static final int TTL_TOKEN_REMEMBER_ME = 60 * 60 * 24 * 14;

    private static final int TTL_TOKEN_XSRF = -1;

    private static final int TTL_TOKEN_EXPIRE = 0;

    public static NewCookie createXSRFToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_XSRF, uriInfo, cookieValue, TTL_TOKEN_XSRF);
    }

    public static NewCookie expireXSRFToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_XSRF, uriInfo, cookieValue, TTL_TOKEN_EXPIRE);
    }

    public static NewCookie createRememberMeToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_REMEMBER_ME, uriInfo, cookieValue, TTL_TOKEN_REMEMBER_ME);
    }

    public static NewCookie expireRememberMeToken(@NotNull final UriInfo uriInfo, @NotNull final String cookieValue) {

        return RestCookieUtils.createToken(COOKIE_TOKEN_REMEMBER_ME, uriInfo, cookieValue, TTL_TOKEN_EXPIRE);
    }

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
