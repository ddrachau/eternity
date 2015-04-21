package com.prodyna.pac.eternity.server.rest.utils;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.UriInfo;

public abstract class RestUtils {

    public static final String JSON_UTF8 = MediaType.APPLICATION_JSON + "; charset=UTF-8";
    public static final String XSRF_HEADER_TOKEN = "X-XSRF-TOKEN";
    public static final String XSRF_COOKIE_TOKEN = "XSRF-TOKEN";
    private static final int TOKEN_TTL = -1;

    public static NewCookie createXSRFToken(@NotNull UriInfo uri, @NotNull String cookieValue) {
        return RestUtils.createXSRFToken(uri, cookieValue, TOKEN_TTL);
    }

    public static NewCookie expireToken(@NotNull UriInfo uri, @NotNull Cookie cookie) {
        return RestUtils.createXSRFToken(uri, cookie.getValue(), 0);
    }

    private static NewCookie createXSRFToken(@NotNull UriInfo uri, @NotNull String cookieValue, int maxAge) {

        String cookieId = XSRF_COOKIE_TOKEN;
        String restPath = uri.getBaseUri().getPath();
        String applicationPath = restPath.substring(0, restPath.indexOf("/", 1));
        String cookiePath = applicationPath;
        String cookieDomain = null;
        String cookieComment = null;
        boolean cookieOnlySecure = false;
        NewCookie cookie = new NewCookie(cookieId, cookieValue, cookiePath, cookieDomain, cookieComment, maxAge, cookieOnlySecure);

        return cookie;

    }

}
