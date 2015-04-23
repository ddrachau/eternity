package com.prodyna.pac.eternity.server.model;

public class RememberMe extends AbstractNode {

    private String token;

    /**
     * Empty default constructor *
     */
    public RememberMe() {

    }

    /**
     * Creates a session and initialize the following properties:
     *
     * @param id    the technical identifier
     * @param token the last accessed time
     */
    public RememberMe(String id, String token) {
        super(id);
        this.token = token;
    }

    /**
     * Creates a session and initialize the following properties:
     *
     * @param token the last accessed time
     */
    public RememberMe(String token) {
        super(null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
