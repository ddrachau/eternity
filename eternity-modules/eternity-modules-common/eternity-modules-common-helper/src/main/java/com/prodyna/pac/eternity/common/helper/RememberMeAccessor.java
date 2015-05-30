package com.prodyna.pac.eternity.common.helper;

/**
 * Helper class for working with RememberMes.
 */
public interface RememberMeAccessor {
    /**
     * The rememberMeTokenDelimiter
     */
    String TOKEN_DELIMITER = ":";

    /**
     * Helper method for extracting the id of a rememberMe from its token.
     *
     * @param rememberMeToken the input token
     * @return the id from the token
     */
    String getRememberMeId(String rememberMeToken);

    /**
     * Helper method for extracting the password of a rememberMe from its token.
     *
     * @param rememberMeToken the input token
     * @return the password from the token
     */
    String getRememberMePassword(String rememberMeToken);

    /**
     * Checks a given string if it might be a rememberMe token
     *
     * @param token the token to check
     * @return true if everything seems legit, false otherwise
     */
    boolean isValidToken(String token);

}
