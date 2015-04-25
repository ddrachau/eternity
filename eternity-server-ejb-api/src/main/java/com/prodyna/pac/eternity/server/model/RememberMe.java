package com.prodyna.pac.eternity.server.model;

import java.util.Calendar;

/**
 * Alternative login token which can be stored for some time.
 */
public class RememberMe extends AbstractNode {

    /**
     * The token to authenticate against the hashedToken.
     */
    private String token;
    /**
     * the stored database hash.
     */
    private String hashedToken;
    /**
     * The initial creation time.
     */
    private Calendar createdTime;

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

    /**
     * Basic Getter
     *
     * @return the createdTime
     */

    public Calendar getCreatedTime() {
        return createdTime;
    }

    /**
     * Basic Setter
     *
     * @param createdTime to be set
     */
    public void setCreatedTime(Calendar createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RememberMe that = (RememberMe) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (hashedToken != null ? !hashedToken.equals(that.hashedToken) : that.hashedToken != null) return false;
        return !(createdTime != null ? !createdTime.equals(that.createdTime) : that.createdTime != null);

    }

    @Override
    public String toString() {
        return "RememberMe{" +
                "token='" + token + '\'' +
                ", hashedToken='" + hashedToken + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }

}
