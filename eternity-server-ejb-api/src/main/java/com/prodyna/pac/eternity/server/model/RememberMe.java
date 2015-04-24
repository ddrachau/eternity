package com.prodyna.pac.eternity.server.model;

public class RememberMe extends AbstractNode {

    private String token;
    private String hashedToken;

    /**
     * Empty default constructor *
     */
    public RememberMe() {

    }

    /**
     * Creates a rememberMe and initialize the following properties:
     *
     * @param id          the technical identifier
     * @param hashedToken the hashed token from the database
     * @param token       the last accessed time
     */
    public RememberMe(String id, String hashedToken, String token) {
        super(id);
        this.token = token;
        this.hashedToken = hashedToken;
    }

    /**
     * Creates a rememberMe and initialize the following properties:
     *
     * @param hashedToken the hashed token from the database
     * @param token       the last accessed time
     */
    public RememberMe(String hashedToken, String token) {
        this(null, hashedToken, token);
    }

    /**
     * Creates a rememberMe and initialize the following properties:
     *
     * @param token the last accessed time
     */
    public RememberMe(String token) {
        this(null, token);
    }

    /**
     * Basic Getter
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Basic Setter
     *
     * @param token to be set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Basic Getter
     *
     * @return the hashedToken
     */
    public String getHashedToken() {
        return hashedToken;
    }

    /**
     * Basic Setter
     *
     * @param hashedToken to be set
     */
    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
    }

}
