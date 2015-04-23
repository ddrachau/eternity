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
     * Creates a session and initialize the following properties:
     *
     * @param id          the technical identifier
     * @param token       the last accessed time
     * @param hashedToken the hashed token from the database
     */
    public RememberMe(String id, String hashedToken, String token) {
        super(id);
        this.token = token;
        this.hashedToken = hashedToken;
    }

    /**
     * Creates a session and initialize the following properties:
     *
     * @param token       the last accessed time
     * @param hashedToken the hashed token from the database
     */
    public RememberMe(String hashedToken, String token) {
        this(null, hashedToken, token);
    }

    /**
     * Creates a session and initialize the following properties:
     *
     * @param token       the last accessed time
     */
    public RememberMe(String token) {
        this(null, token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHashedToken() {
        return hashedToken;
    }

    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
    }
}
