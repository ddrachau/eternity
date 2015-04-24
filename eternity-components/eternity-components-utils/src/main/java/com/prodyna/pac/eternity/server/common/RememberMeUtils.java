package com.prodyna.pac.eternity.server.common;

/**
 * Helper class for working with RememberMes.
 */
public class RememberMeUtils {

    /**
     * The rememberMeTokenDelimiter
     */
    public static final String TOKEN_DELIMITER = ":";

    /**
     * Helper method for extracting the id of a rememberMe from its token.
     *
     * @param rememberMeToken the input token
     * @return the id from the token
     */
    public static String getRememberMeId(String rememberMeToken) {

        return rememberMeToken.split(TOKEN_DELIMITER)[0];

    }

    /**
     * Helper method for extracting the password of a rememberMe from its token.
     *
     * @param rememberMeToken the input token
     * @return the password from the token
     */
    public static String getRememberMePassword(String rememberMeToken) {

        return rememberMeToken.split(TOKEN_DELIMITER)[1];

    }

    /**
     * Checks a given string if it might be a rememberMe token
     *
     * @param token the token to check
     * @return true if everything seems legit, false otherwise
     */
    public static boolean isValidToken(String token) {
        return token != null && token.length() > 10 && token.contains(TOKEN_DELIMITER);
    }

}